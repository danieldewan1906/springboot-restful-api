package com.learning.ecommerce.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.learning.ecommerce.entity.Product;
import com.learning.ecommerce.exception.BadRequestException;
import com.learning.ecommerce.exception.ResourceNotFoundException;
import com.learning.ecommerce.repository.CategoryRepository;
import com.learning.ecommerce.repository.ProductRepository;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository prodRepo;

    @Autowired
    private CategoryRepository cateRepo;
    
    public List<Product> findAll(){
        return prodRepo.findAll();
    }

    public Product findById(String id) {
        return prodRepo.findById(id).orElseThrow(() 
        -> new ResourceNotFoundException("Product with id " + id + " not found"));
    }

    public Product create(Product prod) {
        if (!StringUtils.hasText(prod.getName())) {
            throw new BadRequestException("Nama Product Tidak Boleh Kosong");
        }

        if (prod.getCategory() == null) {
            throw new BadRequestException("Category Tidak Boleh Kosong");
        }

        if (!StringUtils.hasText(prod.getCategory().getId())) {
            throw new BadRequestException("Category ID Tidak Boleh Kosong");
        }

        cateRepo.findById(prod.getCategory().getId()).orElseThrow(() 
        -> new BadRequestException("Category ID " + prod.getCategory().getId() +" Tidak Ditemukan Dalam Database"));

        prod.setId(UUID.randomUUID().toString());
        return prodRepo.save(prod);
    }

    public Product edit(Product prod) {
        if (!StringUtils.hasText(prod.getId())) {
            throw new BadRequestException("Product ID Harus Diisi");
        }

        if (!StringUtils.hasText(prod.getName())) {
            throw new BadRequestException("Nama Product Tidak Boleh Kosong");
        }

        if (prod.getCategory() == null) {
            throw new BadRequestException("Category Tidak Boleh Kosong");
        }

        if (!StringUtils.hasText(prod.getCategory().getId())) {
            throw new BadRequestException("Category ID Tidak Boleh Kosong");
        }

        cateRepo.findById(prod.getCategory().getId()).orElseThrow(() 
        -> new BadRequestException("Category ID " + prod.getCategory().getId() +" Tidak Ditemukan Dalam Database"));
        
        return prodRepo.save(prod);
    }

    public void deleteProduct(String id){
        prodRepo.deleteById(id);
    }

    public Product ubahGambar(String id, String gambar) {
        Product prod = findById(id);
        prod.setImage(gambar);
        return prodRepo.save(prod);
    }

}
