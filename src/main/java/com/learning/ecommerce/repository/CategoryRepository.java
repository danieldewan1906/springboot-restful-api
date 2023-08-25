package com.learning.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.ecommerce.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, String>{
    
}
