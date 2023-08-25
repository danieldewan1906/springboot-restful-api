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

import com.learning.ecommerce.entity.Product;
import com.learning.ecommerce.service.ProductService;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class ProductController {
    
    @Autowired
    private ProductService prodService;

    @GetMapping("/products")
    public List<Product> findAll(){
        return prodService.findAll();
    }

    @GetMapping("/products/{id}")
    public Product findById(@PathVariable("id") String id){
        return prodService.findById(id);
    }

    @PostMapping("/products")
    public Product create(@RequestBody Product prod){
        return prodService.create(prod);
    }

    @PutMapping("/products")
    public Product edit(@RequestBody Product cate){
        return prodService.edit(cate);
    }

    @DeleteMapping("/products/{id}")
    public void deleteCategory(@PathVariable("id") String id){
        prodService.deleteProduct(id);
    }
}
