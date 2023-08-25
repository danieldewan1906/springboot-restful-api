package com.learning.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ecommerce.entity.Orders;
import com.learning.ecommerce.model.OrdersRequest;
import com.learning.ecommerce.model.OrdersResponse;
import com.learning.ecommerce.security.service.UserDetailsImpl;
import com.learning.ecommerce.service.OrdersService;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class OrdersController {
    
    @Autowired
    private OrdersService orderService;

    @PostMapping("/orders")
    @PreAuthorize("hasAuthority('user')")
    public OrdersResponse createOrder(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody OrdersRequest request){
        return orderService.createOrder(user.getUsername(), request);
    }

    @PatchMapping("/orders/{orderId}/cancel")
    @PreAuthorize("hasAuthority('user')")
    public Orders cancelOrder(@AuthenticationPrincipal UserDetailsImpl user, 
        @PathVariable("orderId") String orderId){
        return orderService.cancelOrder(orderId, user.getUsername());
    }

    @PatchMapping("/orders/{orderId}/accept")
    @PreAuthorize("hasAuthority('user')")
    public Orders acceptOrder(@AuthenticationPrincipal UserDetailsImpl user, 
        @PathVariable("orderId") String orderId){
        return orderService.acceptOrder(orderId, user.getUsername());
    }

    @PatchMapping("/orders/{orderId}/confirm")
    @PreAuthorize("hasAuthority('admin')")
    public Orders confirmOrder(@AuthenticationPrincipal UserDetailsImpl user, 
        @PathVariable("orderId") String orderId){
        return orderService.acceptOrder(orderId, user.getUsername());
    }

    @PatchMapping("/orders/{orderId}/packing")
    @PreAuthorize("hasAuthority('admin')")
    public Orders packingOrder(@AuthenticationPrincipal UserDetailsImpl user, 
        @PathVariable("orderId") String orderId){
        return orderService.packingOrder(orderId, user.getUsername());
    }

    @PatchMapping("/orders/{orderId}/ship")
    @PreAuthorize("hasAuthority('admin')")
    public Orders shipOrder(@AuthenticationPrincipal UserDetailsImpl user, 
        @PathVariable("orderId") String orderId){
        return orderService.shipOrder(orderId, user.getUsername());
    }

    @GetMapping("/orders")
    @PreAuthorize("hasAuthority('user')")
    public List<Orders> findAllUserOrder(@AuthenticationPrincipal UserDetailsImpl user, 
    @RequestParam(name = "page", defaultValue = "0", required = false) int page,
    @RequestParam(name = "limit", defaultValue = "25", required = false) int limit) {
        return orderService.findAllOrderUser(user.getUsername(), page, limit);
    }

    @GetMapping("/orders/admin")
    @PreAuthorize("hasAuthority('admin')")
    public List<Orders> search(@AuthenticationPrincipal UserDetailsImpl user, 
    @RequestParam(name = "filterText", defaultValue = "", required = false) String filterText,
    @RequestParam(name = "page", defaultValue = "0", required = false) int page,
    @RequestParam(name = "limit", defaultValue = "25", required = false) int limit) {
        return orderService.search(filterText, page, limit);
    }
}
