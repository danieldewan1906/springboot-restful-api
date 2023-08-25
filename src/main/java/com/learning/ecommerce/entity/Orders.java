package com.learning.ecommerce.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import com.learning.ecommerce.model.StatusOrder;

import lombok.Data;

@Entity
@Data
public class Orders implements Serializable{
    
    @Id
    private String id;

    private String number;

    @Temporal(TemporalType.DATE)
    private Date date;

    @JoinColumn
    @ManyToOne
    private Users user;

    private String address;

    private BigDecimal totalTransaction;

    private BigDecimal shipCost;

    private BigDecimal totalOrder;

    @Enumerated(EnumType.STRING)
    private StatusOrder status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeOrder;
}
