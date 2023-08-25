package com.learning.ecommerce.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.ecommerce.entity.OrderItems;
import com.learning.ecommerce.entity.Orders;
import com.learning.ecommerce.entity.Product;
import com.learning.ecommerce.entity.Users;
import com.learning.ecommerce.exception.BadRequestException;
import com.learning.ecommerce.exception.ResourceNotFoundException;
import com.learning.ecommerce.model.OrdersRequest;
import com.learning.ecommerce.model.OrdersResponse;
import com.learning.ecommerce.model.RequestCart;
import com.learning.ecommerce.model.StatusOrder;
import com.learning.ecommerce.repository.OrderItemsRepository;
import com.learning.ecommerce.repository.OrdersRepository;
import com.learning.ecommerce.repository.ProductRepository;

@Service
public class OrdersService {

    @Autowired
    private ProductRepository prodRepo;

    @Autowired
    private OrdersRepository orderRepo;

    @Autowired
    private OrderItemsRepository orderItemRepo;

    @Autowired
    private CartService cartService;

    @Autowired
    private LogOrderService logOrderService;
    
    @Transactional
    public OrdersResponse createOrder (String username, OrdersRequest request){
        Orders order = new Orders();
        order.setId(UUID.randomUUID().toString());
        order.setDate(new Date());
        order.setNumber(generateNomorPesanan());
        order.setUser(new Users(username));
        order.setAddress(request.getAlamatPengiriman());
        order.setStatus(StatusOrder.DRAFT);
        order.setTimeOrder(new Date());

        List<OrderItems> items = new ArrayList<>();
        for (RequestCart key : request.getItems()) {
            // validasi productnya ada atau tidak
            Product product = prodRepo.findById(key.getProductId()).orElseThrow(()
                            -> new BadRequestException("Product ID " + key.getProductId() + " Tidak Ditemukan"));

            // cek stock cukup atau tidak
            if (product.getStock() < key.getQuantity()) {
                throw new BadRequestException("Stock Product Tidak Cukup");
            }

            OrderItems orderItem = new OrderItems();
            orderItem.setId(UUID.randomUUID().toString());
            orderItem.setProduct(product);
            orderItem.setDescription(product.getDescription());
            orderItem.setQuantity(key.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setTotal(new BigDecimal(orderItem.getPrice().doubleValue() * orderItem.getQuantity()));
            orderItem.setOrder(order);
            items.add(orderItem);
        }

        BigDecimal jumlah = BigDecimal.ZERO;
        for (OrderItems key : items) {
            jumlah = jumlah.add(key.getTotal());
        }

        order.setTotalOrder(jumlah);
        order.setShipCost(request.getOngkir());
        order.setTotalTransaction(order.getTotalOrder().add(order.getShipCost()));

        Orders saved = orderRepo.save(order);
        for (OrderItems orderItems : items) {
            orderItemRepo.save(orderItems);
            Product product = orderItems.getProduct();
            product.setStock(product.getStock() - orderItems.getQuantity());
            prodRepo.save(product);
            cartService.delete(username, product.getId());
        }

        // catat log
        logOrderService.createLog(username, order, logOrderService.DRAFT, "Pesanan Berhasil Dibuat");
        
        OrdersResponse ordersResponse = new OrdersResponse(saved, items);
        return ordersResponse;

    }

    @Transactional
    public Orders cancelOrder (String orderId, String userId) {
        // cek order ada atau tidak
        Orders order = orderRepo.findById(orderId).orElseThrow(()
        -> new ResourceNotFoundException("Order ID " + orderId + " Tidak Ditemukan"));

        if (!userId.equals(order.getUser().getId())) {
            throw new BadRequestException("Pesanan ini hanya dapat dibatalkan oleh yang bersangkutan");
        }

        if (!StatusOrder.DRAFT.equals(order.getStatus())) {
            throw new BadRequestException("Pesanan ini tidak dapat dibatalkan karena sudah diproses");
        }

        order.setStatus(StatusOrder.DIBATALKAN);
        Orders cancel = orderRepo.save(order);
        logOrderService.createLog(userId, cancel, LogOrderService.DIBATALKAN, "Order sukses dibatalkan");
        return cancel;
    }

    @Transactional
    public Orders acceptOrder (String orderId, String userId) {
        // cek order ada atau tidak
        Orders order = orderRepo.findById(orderId).orElseThrow(()
        -> new ResourceNotFoundException("Order ID " + orderId + " Tidak Ditemukan"));

        if (!userId.equals(order.getUser().getId())) {
            throw new BadRequestException("Pesanan ini hanya dapat dibatalkan oleh yang bersangkutan");
        }

        if (!StatusOrder.PENGIRIMAN.equals(order.getStatus())) {
            throw new BadRequestException("Penerimaan Gagal, status pesanan saat ini adalah " + order.getStatus().name());
        }

        order.setStatus(StatusOrder.DIBATALKAN);
        Orders cancel = orderRepo.save(order);
        logOrderService.createLog(userId, cancel, LogOrderService.DIBATALKAN, "Order sukses dibatalkan");
        return cancel;
    }

    public List<Orders> findAllOrderUser(String userId, int page, int limit) {
        return orderRepo.findByUserId(userId, PageRequest.of(page, limit, Sort.by("timeOrder").descending()));
    }

    public List<Orders> search(String filterText, int page, int limit){
        return orderRepo.search(filterText.toLowerCase(), PageRequest.of(page, limit, Sort.by("timeOrder").descending()));
    }

    @Transactional
    public Orders confirmOrder (String orderId, String userId) {
        // cek order ada atau tidak
        Orders order = orderRepo.findById(orderId).orElseThrow(()
        -> new ResourceNotFoundException("Order ID " + orderId + " Tidak Ditemukan"));

        if (!StatusOrder.DRAFT.equals(order.getStatus())) {
            throw new BadRequestException("Konfirmasi pesanan gagal, status pesanan saat ini adalah " + order.getStatus().name());
        }

        order.setStatus(StatusOrder.PEMBAYARAN);
        Orders cancel = orderRepo.save(order);
        logOrderService.createLog(userId, cancel, LogOrderService.PEMBAYARAN, "Pembayaran sukses dikonfirmasi");
        return cancel;
    }

    @Transactional
    public Orders packingOrder (String orderId, String userId) {
        // cek order ada atau tidak
        Orders order = orderRepo.findById(orderId).orElseThrow(()
        -> new ResourceNotFoundException("Order ID " + orderId + " Tidak Ditemukan"));

        if (!StatusOrder.PEMBAYARAN.equals(order.getStatus())) {
            throw new BadRequestException("Packing pesanan gagal, status pesanan saat ini adalah " + order.getStatus().name());
        }

        order.setStatus(StatusOrder.PACKING);
        Orders cancel = orderRepo.save(order);
        logOrderService.createLog(userId, cancel, LogOrderService.PACKING, "Pesanan sedang disiapkan");
        return cancel;
    }

    @Transactional
    public Orders shipOrder (String orderId, String userId) {
        // cek order ada atau tidak
        Orders order = orderRepo.findById(orderId).orElseThrow(()
        -> new ResourceNotFoundException("Order ID " + orderId + " Tidak Ditemukan"));

        if (!StatusOrder.PACKING.equals(order.getStatus())) {
            throw new BadRequestException("Pengiriman pesanan gagal, status pesanan saat ini adalah " + order.getStatus().name());
        }

        order.setStatus(StatusOrder.PENGIRIMAN);
        Orders cancel = orderRepo.save(order);
        logOrderService.createLog(userId, cancel, LogOrderService.PENGIRIMAN, "Pesanan sedang dikirim");
        return cancel;
    }


    private String generateNomorPesanan() {
        return String.format("%016d", System.nanoTime());
    }
}
