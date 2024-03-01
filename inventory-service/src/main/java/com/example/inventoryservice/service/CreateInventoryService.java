package com.example.inventoryservice.service;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
public class CreateInventoryService {
    public void createInventory(Exchange exchange) throws Exception {
//        Thread.sleep(10000);
        System.out.println("creating Inventory");
        Map<String, Object> map = new HashMap<>();
        map.put("message", "yoo! Inventory added successfully");

        throw new Exception();
//        exchange.getMessage().setBody(map);
    }

    public void completeInventory(Exchange exchange) throws Exception {
        System.out.println("Completing Inventory");
        Map<String, Object> map = new HashMap<>();
        map.put("message", "yoo! Inventory completed");
//        throw new Exception();
        exchange.getMessage().setBody(map);
    }
}
