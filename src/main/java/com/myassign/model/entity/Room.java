package com.myassign.model.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    private String id;

    private String name;

    private String password;

    private String createDatetime;
}