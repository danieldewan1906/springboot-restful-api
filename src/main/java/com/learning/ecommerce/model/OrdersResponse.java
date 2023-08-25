package com.learning.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.learning.ecommerce.entity.OrderItems;
import com.learning.ecommerce.entity.Orders;

import lombok.Data;

@Data
public class OrdersResponse implements Serializable {
    
    private String id;
    private String nomorPesanan;
    private Date tanggal;
    private String namaPelanggan;
    private String alamatPengiriman;
    private Date waktuPesanan;
    private BigDecimal jumlah;
    private BigDecimal ongkir;
    private BigDecimal total;
    private List<OrdersResponse.Item> items;

    public OrdersResponse(Orders order, List<OrderItems> orderItem) {
        this.id = order.getId();
        this.nomorPesanan = order.getNumber();
        this.tanggal = order.getDate();
        this.namaPelanggan = order.getUser().getName();
        this.alamatPengiriman = order.getAddress();
        this.waktuPesanan = order.getTimeOrder();
        this.jumlah = order.getTotalOrder();
        this.ongkir = order.getShipCost();
        this.total = order.getTotalTransaction();
        items = new ArrayList<>();
        for (OrderItems orderItems : orderItem) {
            Item item = new Item();
            item.setProductId(orderItems.getProduct().getId());
            item.setNamaProduct(orderItems.getProduct().getName());
            item.setQuantity(orderItems.getQuantity());
            item.setHarga(orderItems.getPrice());
            item.setJumlah(orderItems.getTotal());
            items.add(item);

        }
    }

    @Data
    public static class Item implements Serializable {
        private String productId;
        private String namaProduct;
        private Double quantity;
        private BigDecimal harga;
        private BigDecimal jumlah;
    }
}
