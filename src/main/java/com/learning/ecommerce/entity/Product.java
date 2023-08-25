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
public class Product implements Serializable{
    
    @Id
    private String id;

    private String name;

    private String description;

    private String image;

    @JoinColumn
    @ManyToOne
    private Category category;

    private BigDecimal price;
    
    private Double stock;

}
