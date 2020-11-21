package com.myassign.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myassign.exception.RoomNotExistException;
import com.myassign.exception.TransactionExpiredException;
import com.myassign.exception.TransactionPermissionException;
import com.myassign.exception.TransactoinNotFoundException;
import com.myassign.exception.UserNotFoundException;
import com.myassign.model.dto.TransactionResultDto;
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
                                             .createDate(new Date())
                                             .build();
        /* @formatter:on */

        log.info("room : {}", room);
        log.info("user : {}", user);
        transaction = transactionRepository.save(transaction);
        log.info("new transaction : {}, {}", transaction.getId(), transaction.getToken());

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
            log.info("({}) transaction user : {}", i, transactionUser);
            transactionUser = transactionUserRepository.save(transactionUser);
            log.info("transactionUser : {}", transactionUser);
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
        /* @formatter:on */

        log.info("room : {}", room);
        log.info("user : {}", user);

        /* @formatter:off */
        Transaction transaction = transactionRepository.findByRoomAndToken(room, token)
                                                       .orElseThrow(() -> {
                                                           throw new TransactoinNotFoundException(roomId, token);
                                                       });
        /* @formatter:on */

        if (transaction.getSpreadUser().getId().equals(userId)) {
            throw new TransactionPermissionException();
        }

        Duration duration = Duration.between(LocalDateTime.now(), transaction.getCreateDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        long diff = Math.abs(duration.toMinutes());

        // 뿌리기 이후 10분이 경과한 경우 받기 불가 처리
        if (diff > 10) {
            log.info("Request transaction is expired. 10 minutes, roomId : {}, token : {}", roomId, token);
            throw new TransactionExpiredException(token);
        }

        TransactionUser transactionUser = transactionUserRepository.findTop1ByTransactionAndReceiveUserIdNullOrderByOrderAsc(transaction);
        log.info("updatable transaction, token : {}, request user : {}", transaction.getToken(), userId);

        // 받은 금액의 사용자 데이터 변경
        long price = transactionUser.getPrice();
        transactionUser.setReceiveUserId(userId);
        transactionUser.setReceiveDate(new Date());
        transactionUserRepository.save(transactionUser);

        // 받은 사용자의 잔고 데이터 변경
        user.setBalance(user.getBalance() + price);
        userRepository.save(user);

        /* @formatter:off */
        TransactionResultDto result = TransactionResultDto.builder()
                                                          .roomId(roomId)
                                                          .token(token)
                                                          .roomName(room.getName())
                                                          .price(price)
                                                          .receiveDate(transactionUser.getReceiveDate())
                                                          .build();
        /* @formatter:on */

        return result;
    }

    /**
     * 뿌리기 조회
     */
    public Transaction getTransaction(UUID roomId, String token) {

        /* @formatter:off */
        Room room = roomRepository.findById(roomId)
                                  .orElseThrow(() -> new RoomNotExistException(roomId));

        Transaction transaction = transactionRepository.findByRoomAndToken(room, token)
                                                       .orElseThrow(() -> {
                                                           throw new TransactoinNotFoundException(roomId, token);
                                                       });
        /* @formatter:on */

        return transaction;
    }
}