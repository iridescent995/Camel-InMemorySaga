package com.example.inventoryservice.routes;

import com.example.inventoryservice.service.CancelInventoryService;
import com.example.inventoryservice.service.CreateInventoryService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.SagaPropagation;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.saga.InMemorySagaService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InventoryRoute extends RouteBuilder {

    private final CreateInventoryService createInventoryService;
    private final CancelInventoryService cancelInventoryService;

    public InventoryRoute(CreateInventoryService createInventoryService, CancelInventoryService cancelInventoryService) {
        this.createInventoryService = createInventoryService;
        this.cancelInventoryService = cancelInventoryService;
    }

    @Override
    public void configure() throws Exception {

        getContext().addService(new InMemorySagaService());

        restConfiguration().component("servlet")
                .bindingMode(RestBindingMode.json);

        from("direct:createInventory")
                .process(
                        exchange -> {
                            exchange.getMessage().setHeader("id", UUID.randomUUID().toString());

                        }).saga().to("direct:newInventory");

        from("direct:newInventory").saga()
                .propagation(SagaPropagation.MANDATORY)
                .option("id", header("id"))
                .compensation("direct:cancelInventory")
                .completion("direct:completeInventory")
                .bean(createInventoryService, "createInventory");

        from("direct:completeInventory").bean(createInventoryService, "completeInventory");

        from("direct:cancelInventory").bean(cancelInventoryService, "cancelInventory");

    }
}