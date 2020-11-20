package com.myassign.model.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Builder
@ToString
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id", columnDefinition = "VARCHAR", length = 128)
    private String id;

    @Column(name = "user_name", columnDefinition = "VARCHAR", length = 128)
    private String name;

    @Column(name = "password", columnDefinition = "VARCHAR", length = 512)
    private String password;

    @Column(name = "balance", columnDefinition = "BIGINT", length = 20)
    private Long balance;

    @OneToMany(mappedBy = "user")
    private List<RoomUser> roomUserList;

    @Column(name = "create_date", updatable = false)
    private Date createDate;
}