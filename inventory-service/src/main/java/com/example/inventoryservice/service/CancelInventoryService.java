package com.example.inventoryservice.service;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.apache.camel.builder.Builder.constant;

@Component
public class CancelInventoryService {
    public void cancelInventory(Exchange exchange){
        System.out.println("Canceling Inventory");
        Map<String, Object> map = new HashMap<>();
        map.put("message", "oh No! Inventory canceled");
        exchange.getMessage().setBody(map);
        exchange.getMessage().setHeader("service", constant("inventory"));
    }
}
