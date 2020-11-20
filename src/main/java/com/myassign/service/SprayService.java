package com.myassign.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myassign.exception.RoomNotExistException;
import com.myassign.exception.UserNotFoundException;
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
public class SprayService {

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
}