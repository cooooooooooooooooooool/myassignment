package com.myassign.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoomUserDto {

    private Long id;

    private RoomDto room;

    private UserDto user;

    private Date joinDate;
}
