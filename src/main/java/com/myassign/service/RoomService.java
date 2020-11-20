package com.myassign.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myassign.exception.RoomNotExistException;
import com.myassign.exception.UserNotFoundException;
import com.myassign.model.entity.Room;
import com.myassign.model.entity.RoomUser;
import com.myassign.model.entity.User;
import com.myassign.model.repository.RoomRepository;
import com.myassign.model.repository.RoomUserRepository;
import com.myassign.model.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomUserRepository roomUserRepository;
    private final UserRepository userRepository;

    /**
     * 채팅방 생성
     */
    @Transactional
    public Room createRoom(String roomName) {
        Room room = Room.builder().name(roomName).createDate(new Date()).build();
        return roomRepository.save(room);
    }

    public Room getTopRoom() {
        List<Room> list = roomRepository.findAll();

        if (list == null)
            throw new RoomNotExistException("There is no room!");

        return list.get(0);
    }

    @Transactional
    public void joinRoomUser(UUID roomId, String userId) {
        /* @formatter:off */
        Room room = roomRepository.findById(roomId)
                                  .orElseThrow(() -> new RoomNotExistException(roomId));
        
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new UserNotFoundException(userId));
        /* @formatter:on */

        RoomUser roomUser = RoomUser.builder().joinDate(new Date()).user(user).room(room).build();
        roomUserRepository.save(roomUser);
    }
}