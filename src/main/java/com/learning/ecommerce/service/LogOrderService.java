package com.learning.ecommerce.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.ecommerce.entity.LogOrder;
import com.learning.ecommerce.entity.Orders;
import com.learning.ecommerce.entity.Users;
import com.learning.ecommerce.repository.LogOrderRepository;

@Service
public class LogOrderService {
    
    @Autowired
    private LogOrderRepository logOrderRepo;

    public final static int DRAFT = 0;
    public final static int PEMBAYARAN = 10;
    public final static int PACKING = 20;
    public final static int PENGIRIMAN = 30;
    public final static int SELESAI = 40;
    public final static int DIBATALKAN = 90;

    public void createLog(String username, Orders order, int type, String msg) {
        LogOrder log = new LogOrder();
        log.setId(UUID.randomUUID().toString());
        log.setLogMessage(msg);
        log.setLogType(type);
        log.setOrder(order);
        log.setUser(new Users(username));
        log.setDate(new Date());
        logOrderRepo.save(log);
    }
}
