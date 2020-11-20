package com.myassign.model.dto;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Table;

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
@Table(name = "room")
public class RoomDto {

    private UUID id;

    private String name;

    private Date createDate;
}