package com.learning.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.ecommerce.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
    
}
