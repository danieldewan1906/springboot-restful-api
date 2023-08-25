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

import com.learning.ecommerce.entity.Category;
import com.learning.ecommerce.service.CategoryService;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class CategoryController {

    @Autowired
    private CategoryService cateService;

    @GetMapping("/categories")
    public List<Category> findAll(){
        return cateService.findAll();
    }

    @GetMapping("/categories/{id}")
    public Category findById(@PathVariable("id") String id){
        return cateService.findById(id);
    }

    @PostMapping("/categories")
    public Category create(@RequestBody Category cate){
        return cateService.create(cate);
    }

    @PutMapping("/categories")
    public Category edit(@RequestBody Category cate){
        return cateService.edit(cate);
    }

    @DeleteMapping("/categories/{id}")
    public void deleteCategory(@PathVariable("id") String id){
        cateService.deleteCategory(id);
    }

}
