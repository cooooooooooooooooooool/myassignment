package com.myassign.model.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id", columnDefinition = "varchar(128)")
    private String id;

    @Column(name = "user_name", columnDefinition = "varchar(256)")
    private String name;

    @Column(name = "password", columnDefinition = "varchar(512)")
    private String password;

    @Column(name = "balance", columnDefinition = "bigint(20)")
    private Long balance;

    @OneToMany(mappedBy = "user")
    private List<RoomUser> roomUserList;

    @Column(name = "create_date", updatable = false)
    private Date createDate;
}