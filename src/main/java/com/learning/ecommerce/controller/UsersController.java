package com.learning.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ecommerce.entity.Users;
import com.learning.ecommerce.service.UsersService;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class UsersController {
    
    @Autowired
    private UsersService userService;

    @GetMapping("/users")
    public List<Users> findAll(){
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public Users findById(@PathVariable("id") String id){
        return userService.findById(id);
    }

    @PostMapping("/users")
    public Users create(@RequestBody Users user){
        return userService.create(user);
    }

    @PutMapping("/users")
    public Users edit(@RequestBody Users user){
        return userService.edit(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteCategory(@PathVariable("id") String id){
        userService.deleteUsers(id);
    }
}
