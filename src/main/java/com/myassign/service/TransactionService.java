package com.myassign.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Minutes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myassign.exception.RoomNotExistException;
import com.myassign.exception.TransactionExpiredException;
import com.myassign.exception.TransactionNotFoundException;
import com.myassign.exception.TransactionPermissionException;
import com.myassign.exception.UserNotFoundException;
import com.myassign.model.dto.TransactionReceiveUserDto;
import com.myassign.model.dto.TransactionResultDto;
import com.myassign.model.dto.TransactionStatusDto;
import com.myassign.model.entity.Room;
import com.myassign.model.entity.Transaction;
import com.myassign.model.entity.TransactionUser;
import com.myassign.model.entity.User;
import com.myassign.model.repository.RoomRepository;
import com.myassign.model.repository.TransactionRepository;
import com.myassign.model.repository.TransactionUserRepository;
import com.myassign.model.repository.UserRepository;
import com.myassign.util.TransactionTokenGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionUserRepository transactionUserRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    /**
     * 뿌리기 처리
     */
    @Transactional
    public String sprayPrice(String userId, UUID roomId, int totalPrice, int userCount) {

        log.info("userId : {}", userId);

        /* @formatter:off */
        Room room = roomRepository.findById(roomId)
                                  .orElseThrow(() -> new RoomNotExistException(roomId));
        
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new UserNotFoundException(userId));
        
        Transaction transaction = Transaction.builder()
                                             .room(room)
                                             .token(TransactionTokenGenerator.generateToken())
                                             .totalPrice(Long.valueOf(totalPrice))
                                             .spreadUser(user)
                                             .currentReceivePrice(0L)
                                             .createDate(new Date())
                                             .build();
        /* @formatter:on */

        transaction = transactionRepository.save(transaction);

        // 분배 처리
        int userPrice = totalPrice / userCount;
        for (int i = 0; i < userCount; i++) {
            /* @formatter:off */
            TransactionUser transactionUser = TransactionUser.builder()
                                                             .order(i)
                                                             .price(i==0 ? Long.valueOf(userPrice + (totalPrice % userCount)) : Long.valueOf(userPrice))
                                                             .transaction(transaction)
                                                             .createDate(new Date())
                                                             .build();
            /* @formatter:on */
            transactionUserRepository.save(transactionUser);
        }

        // 잔액 변경 처리
        user.setBalance(user.getBalance() - Long.valueOf(totalPrice));
        userRepository.save(user);

        return transaction.getToken();
    }

    /**
     * 받기
     */
    @Transactional
    public TransactionResultDto receivePrice(String userId, UUID roomId, String token) {

        log.info("receive userId : {}", userId);

        /* @formatter:off */
        Room room = roomRepository.findById(roomId)
                                  .orElseThrow(() -> new RoomNotExistException(roomId));
        
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new UserNotFoundException(userId));

        Transaction transaction = transactionRepository.findByRoomAndToken(room, token)
                                                       .orElseThrow(() -> {
                                                           throw new TransactionNotFoundException(roomId, token);
                                                       });
        /* @formatter:on */

        if (transaction.getSpreadUser().getId().equals(userId)) {
            throw new TransactionPermissionException();
        }

        int diff = Minutes.minutesBetween(new DateTime(transaction.getCreateDate().getTime()), DateTime.now()).getMinutes();

        // 뿌리기 이후 10분이 경과한 경우 받기 불가 처리
        if (diff > 10) {
            log.info("Request transaction is expired. 10 minutes, roomId : {}, token : {}", roomId, token);
            throw new TransactionExpiredException(token, roomId);
        }

        TransactionUser transactionUser = transactionUserRepository.findTop1ByTransactionAndReceiveUserIdNullOrderByOrderAsc(transaction);
        log.info("updatable transaction, token : {}, request user : {}", transaction.getToken(), userId);

        long price = transactionUser.getPrice();

        // 받은 금액 누적 데이터 변경
        transaction.setCurrentReceivePrice(transaction.getCurrentReceivePrice() + price);
        transactionRepository.save(transaction);

        // 받은 금액의 사용자 데이터 변경
        transactionUser.setReceiveUserId(userId);
        transactionUser.setReceiveDate(new Date());
        transactionUserRepository.save(transactionUser);

        // 받은 사용자의 잔고 데이터 변경
        user.setBalance(user.getBalance() + price);
        userRepository.save(user);

        /* @formatter:off */
        TransactionResultDto result = TransactionResultDto.builder()
                                                          .token(token)
                                                          .price(price)
                                                          .receiveDate(transactionUser.getReceiveDate())
                                                          .build();
        /* @formatter:on */

        return result;
    }

    /**
     * 뿌리기 상태 조회
     */
    public TransactionStatusDto getTransactionStatus(String userId, UUID roomId, String token) {

        /* @formatter:off */
        Room room = roomRepository.findById(roomId)
                                  .orElseThrow(() -> new RoomNotExistException(roomId));

        Transaction transaction = transactionRepository.findByRoomAndToken(room, token)
                                                       .orElseThrow(() -> {
                                                           throw new TransactionNotFoundException(roomId, token);
                                                       });
        /* @formatter:on */

        if (!transaction.getSpreadUser().getId().equals(userId)) {
            throw new TransactionPermissionException();
        }

        int diff = Days.daysBetween(new DateTime(transaction.getCreateDate().getTime()), DateTime.now()).getDays();
        // int diff = Minutes.minutesBetween(new
        // DateTime(transaction.getCreateDate().getTime()),
        // DateTime.now()).getMinutes();

        // 만 7일이 지난 뿌리기는 조회 불가, 여기에서는 1분
        if (diff > 7) {
            log.info("Request transaction data exceed 7 day, roomId : {}, token : {}", roomId, token);
            throw new TransactionExpiredException("Request transaction data exceed 7 day. token : " + token);
        }

        // 뿌리기 받은 데이터 조회
        List<TransactionUser> list = transactionUserRepository.findByTransactionOrderByOrderAsc(transaction);

        /* @formatter:off */
        List<TransactionReceiveUserDto> receiverUsers = list.stream()
                                                            .filter(x -> StringUtils.isNotEmpty(x.getReceiveUserId()))
                                                            .map(e -> {
                                                                return TransactionReceiveUserDto.builder().price(e.getPrice()).userId(e.getReceiveUserId()).build();
                                                            })
                                                            .collect(Collectors.toList());
        
        
        TransactionStatusDto dto = TransactionStatusDto.builder()
                                                       .receiveFinishPrice(transaction.getCurrentReceivePrice())
                                                       .sprayDate(transaction.getCreateDate())
                                                       .sprayPrice(transaction.getTotalPrice())
                                                       .receiverUsers(receiverUsers)
                                                       .build();
        /* @formatter:on */

        return dto;
    }
}