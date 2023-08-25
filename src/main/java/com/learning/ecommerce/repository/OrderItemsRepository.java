package com.learning.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.ecommerce.entity.OrderItems;

public interface OrderItemsRepository extends JpaRepository<OrderItems, String>{
    
}
