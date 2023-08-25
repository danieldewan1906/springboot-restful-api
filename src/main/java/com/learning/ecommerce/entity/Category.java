package com.learning.ecommerce.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
public class Category implements Serializable{
    
    @Id
    private String id;
    
    private String name;
}
