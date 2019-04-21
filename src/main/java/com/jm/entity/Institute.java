package com.jm.entity;

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
@Table(name = "institute")
@NoArgsConstructor
@AllArgsConstructor
public class Institute {

    @Id
    private String code;
    
    private String name;
}