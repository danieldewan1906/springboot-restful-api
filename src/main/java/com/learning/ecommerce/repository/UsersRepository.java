package com.learning.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.ecommerce.entity.Users;

public interface UsersRepository extends JpaRepository<Users, String>{
    
    Boolean existsByEmail(String email);
    
}
