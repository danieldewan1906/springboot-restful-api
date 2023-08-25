package com.learning.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.learning.ecommerce.entity.Users;
import com.learning.ecommerce.exception.BadRequestException;
import com.learning.ecommerce.exception.ResourceNotFoundException;
import com.learning.ecommerce.repository.UsersRepository;

@Service
public class UsersService {
    
    @Autowired
    private UsersRepository userRepo;

    public Users findById(String id){
        return userRepo.findById(id).orElseThrow(()
        -> new ResourceNotFoundException("User with id " + id +" not found"));
    }

    public List<Users> findAll() {
        return userRepo.findAll();
    }

    public Users create(Users user) {
        if (!StringUtils.hasText(user.getId())) {
            throw new BadRequestException("Username Tidak Boleh Kosong");
        }

        if (userRepo.existsById(user.getId())) {
            throw new BadRequestException("Username " +user.getId()+ " Sudah Terdaftar");
        }

        if (!StringUtils.hasText(user.getEmail())) {
            throw new BadRequestException("Email Tidak Boleh Kosong");
        }

        if (userRepo.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email "+user.getEmail()+" Sudah Terdaftar");
        }

        user.setIsActive(true);
        return userRepo.save(user);
    }

    public Users edit(Users user) {
        if (!StringUtils.hasText(user.getId())) {
            throw new BadRequestException("Username Tidak Boleh Kosong");
        }

        if (!StringUtils.hasText(user.getEmail())) {
            throw new BadRequestException("Email Tidak Boleh Kosong");
        }

        return userRepo.save(user);
    }

    public void deleteUsers(String id){
        userRepo.deleteById(id);
    }
}
