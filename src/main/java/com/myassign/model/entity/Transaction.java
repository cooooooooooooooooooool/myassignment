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

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction", uniqueConstraints = @UniqueConstraint(columnNames = { "room_id", "token" }))
public class Transaction {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "com.myassign.model.UUIDTimebaseGenerator")
    @Column(name = "transaction_id", columnDefinition = "CHAR", length = 36)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", referencedColumnName = "room_id", nullable = false)
    private Room room;

    @Column(name = "token", columnDefinition = "CHAR", length = 3, nullable = false)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spread_user", referencedColumnName = "user_id", nullable = false)
    private User spreadUser;

    @Column(name = "total_price", columnDefinition = "BIGINT", length = 20, nullable = false)
    private Long totalPrice;

    @Column(name = "current_receive_price", columnDefinition = "BIGINT", length = 20, nullable = false)
    private Long currentReceivePrice;

    @OneToMany(mappedBy = "transaction")
    private List<TransactionUser> transactionUserList;

    @Column(name = "create_date", updatable = false)
    private Date createDate;
}