package com.myassign.model.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room")
public class Room {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "com.myassign.model.UUIDTimebaseGenerator")
    @Column(name = "room_id", columnDefinition = "CHAR", length = 36)
    private UUID id;

    @Column(name = "room_name", columnDefinition = "VARCHAR", length = 255)
    private String name;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<RoomUser> roomUserList;

    @Column(name = "create_date", updatable = false)
    private Date createDate;
}