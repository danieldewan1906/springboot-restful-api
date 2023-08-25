package com.learning.ecommerce.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.ecommerce.entity.Category;
import com.learning.ecommerce.exception.ResourceNotFoundException;
import com.learning.ecommerce.repository.CategoryRepository;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository cateRepo;

    public Category findById(String id){
        return cateRepo.findById(id).orElseThrow(() -> 
        new ResourceNotFoundException("Category with id " + id + " not found"));
    }

    public List<Category> findAll() {
        return cateRepo.findAll();
    }

    public Category create(Category cate) {
        cate.setId(UUID.randomUUID().toString());
        return cateRepo.save(cate);
    }

    public Category edit(Category cate) {
        return cateRepo.save(cate);
    }

    public void deleteCategory(String id){
        cateRepo.deleteById(id);
    }
}
