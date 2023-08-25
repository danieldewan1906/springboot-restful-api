package com.learning.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.ecommerce.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, String>{

    Optional<Cart> findByUserIdAndProductId(String username, String prodId);

    List<Cart> findByUserId(String username);
    
}
