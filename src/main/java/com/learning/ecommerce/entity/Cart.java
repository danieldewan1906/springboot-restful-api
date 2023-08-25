package com.learning.ecommerce.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Data
public class Cart implements Serializable{
    
    @Id
    private String id;

    @JoinColumn
    @ManyToOne
    private Product product;

    @JoinColumn
    @ManyToOne
    private Users user;

    private Double quantity;

    private BigDecimal price;

    private BigDecimal total;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeMade;
}
