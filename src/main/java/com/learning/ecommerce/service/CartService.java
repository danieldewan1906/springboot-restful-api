package com.learning.ecommerce.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.ecommerce.entity.Cart;
import com.learning.ecommerce.entity.Product;
import com.learning.ecommerce.entity.Users;
import com.learning.ecommerce.exception.BadRequestException;
import com.learning.ecommerce.repository.CartRepository;
import com.learning.ecommerce.repository.ProductRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private ProductRepository prodRepo;

    @Transactional
    public Cart addToCart(String username, String prodId, Double qty){
        // apakah product ada dalam database
        // apakah sudah ada dalam keranjang milik user
        // jika sudah maka update qty dan hitung 
        // jika belum ada tambah baru

        Product prod = prodRepo.findById(prodId).orElseThrow(()
                -> new BadRequestException("Product ID "+ prodId + " Tidak Ditemukan"));

        Optional<Cart> optional = cartRepo.findByUserIdAndProductId(username, prodId);

        Cart cart;
        if (optional.isPresent()) {
            cart = optional.get();
            cart.setQuantity(cart.getQuantity() + qty);
            cart.setTotal(new BigDecimal(cart.getPrice().doubleValue() * cart.getQuantity()));
            cartRepo.save(cart);
        } else {
            cart = new Cart();
            cart.setId(UUID.randomUUID().toString());
            cart.setProduct(prod);
            cart.setQuantity(qty);
            cart.setPrice(prod.getPrice());
            cart.setTotal(new BigDecimal(cart.getPrice().doubleValue() * cart.getQuantity()));
            cart.setUser(new Users(username));
            cartRepo.save(cart);
        }

        return cart;
    }

    @Transactional
    public Cart updateQuantity(String username, String prodId, Double qty){
        Cart cart = cartRepo.findByUserIdAndProductId(username, prodId).orElseThrow(()
                    -> new BadRequestException("Product ID "+ prodId + " Tidak Ditemukan Dalam Keranjang Anda"));

        cart.setQuantity(qty);
        cart.setTotal(new BigDecimal(cart.getPrice().doubleValue() * cart.getQuantity()));
        cartRepo.save(cart);
        return cart;
    }

    @Transactional
    public void delete(String username, String prodId){
        Cart cart = cartRepo.findByUserIdAndProductId(username, prodId).orElseThrow(()
                    -> new BadRequestException("Product ID "+ prodId + " Tidak Ditemukan Dalam Keranjang Anda"));

        cartRepo.delete(cart);
    }

    public List<Cart> listCartUser(String username){
        return cartRepo.findByUserId(username);
    }
    
}
