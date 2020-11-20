package com.myassign.model.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myassign.model.entity.Room;

public interface RoomRepository extends JpaRepository<Room, UUID> {
    public Room findTop1ByOrderByNameAsc();
}