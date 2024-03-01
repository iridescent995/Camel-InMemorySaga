package com.example.inventoryservice.web;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryController {
    @Autowired
    ProducerTemplate producerTemplate;

    @GetMapping(value = "createInventory", produces = "application/json")
    public ResponseEntity<String> createOrder(@RequestHeader(value="service") String service){
        try {
            System.out.println("Welcome to Inventory service");
            System.out.println("service header>>>>" + service);
            HttpHeaders headers = new HttpHeaders();
            headers.add("service", "inventory");

            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(producerTemplate.requestBody("direct:createInventory","").toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @GetMapping(value = "cancelInventory", produces = "application/json")
    public ResponseEntity<String> cancelOrder(){
        try {
            System.out.println("Invoking Cancel Inventory service");
            return ResponseEntity.status(HttpStatus.OK).body(producerTemplate.requestBody("direct:cancelInventory","").toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
