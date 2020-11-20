package com.myassign.model.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

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
@Table(name = "transaction", uniqueConstraints = @UniqueConstraint(columnNames = { "room_id", "token" }))
public class Transaction {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "com.myassign.model.UUIDTimebaseGenerator")
    @Column(name = "transaction_id", columnDefinition = "char(36)")
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", referencedColumnName = "room_id", nullable = false)
    private Room room;

    @Column(name = "token", columnDefinition = "char(3)", nullable = false)
    private String token;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "send_user", referencedColumnName = "user_id", nullable = false)
    private User sendUser;

    @Column(name = "total_price", columnDefinition = "bigint(20)", nullable = false)
    private Long totalPrice;

    @OneToMany(mappedBy = "transaction")
    private List<TransactionUser> transactionUserList;

    @Column(name = "create_date", updatable = false)
    private Date createDate;
}