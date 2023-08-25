package com.learning.ecommerce.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class OrderItems implements Serializable{
    
    @Id
    private String id;

    @JoinColumn
    @ManyToOne
    private Orders order;

    @JoinColumn
    @ManyToOne
    private Product product;

    private String description;

    private Double quantity;

    private BigDecimal price;

    private BigDecimal total;
}
