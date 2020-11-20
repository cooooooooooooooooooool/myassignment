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

import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction_user", uniqueConstraints = @UniqueConstraint(columnNames = { "transaction_id", "receive_user" }))
public class TransactionUser {

    @Id
    @Column(name = "id", columnDefinition = "BIGINT", length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @Column(name = "receive_user", columnDefinition = "VARCHAR", length = 128)
    private String receiveUserId;

    @Column(name = "order", columnDefinition = "INT", length = 8, nullable = false)
    private Integer order;

    @Column(name = "price", columnDefinition = "BIGINT", length = 20)
    private Long price;

    @Column(name = "receive_date")
    private Date receiveDate;

    @Column(name = "create_date")
    private Date createDate;
}
