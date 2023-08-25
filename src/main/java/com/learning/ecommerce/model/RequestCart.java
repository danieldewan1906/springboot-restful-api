package com.learning.ecommerce.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class RequestCart implements Serializable{
    
    private String productId;
    private Double quantity;
}
