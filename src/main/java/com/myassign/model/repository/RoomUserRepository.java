package com.myassign.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myassign.model.entity.RoomUser;

public interface RoomUserRepository extends JpaRepository<RoomUser, Long> {
}