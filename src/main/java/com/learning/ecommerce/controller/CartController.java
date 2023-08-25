package com.learning.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ecommerce.entity.Cart;
import com.learning.ecommerce.model.RequestCart;
import com.learning.ecommerce.security.service.UserDetailsImpl;
import com.learning.ecommerce.service.CartService;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class CartController {
    
    @Autowired
    private CartService cartService;

    @GetMapping("/cart")
    public List<Cart> findByUserId(@AuthenticationPrincipal UserDetailsImpl user){
        return cartService.listCartUser(user.getUsername());
    }

    @PostMapping("/cart")
    public Cart create(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody RequestCart requestCart){
        return cartService.addToCart(user.getUsername(), requestCart.getProductId(), requestCart.getQuantity());
    }

    @PatchMapping("/cart/{prodId}")
    public Cart update(@AuthenticationPrincipal UserDetailsImpl user
    , @PathVariable("prodId") String prodId, @RequestParam("quantity") Double qty) {
        return cartService.updateQuantity(user.getUsername(), prodId, qty);
    }

    @DeleteMapping("/cart/{prodId}")
    public void delete(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable("prodId") String prodId){
        cartService.delete(user.getUsername(), prodId);
    }
}
