package com.myassign.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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
@Table(name = "transaction_user", uniqueConstraints = @UniqueConstraint(columnNames = { "transaction_id", "user_id" }))
public class TransactionUser {

    @Id
    @Column(name = "id", columnDefinition = "bigint(20)")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "price", columnDefinition = "bigint(20)")
    private Long price;

    @Column(name = "send_date", updatable = false)
    private Date sendDate;

    @Column(name = "receive_date")
    private Date receiveDate;
}
