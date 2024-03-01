# Camel-InMemorySaga
This Example shows how to configure IN-MEMORY SAGA SERVICE using Apache Camel

## Apache Camel Documentation 
https://camel.apache.org/components/4.4.x/eips/saga-eip.html#_using_the_in_memory_saga_service

In this example we will create an inventory service, using Spring Boot and Apache Camel DSL. 
We will create 1 route `direct:createInventory` with 2 saga routes for `compensation` and `completion`

```
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
```
