# Camel-InMemorySaga
This Example shows how to configure IN-MEMORY SAGA SERVICE using Apache Camel

### Apache Camel Documentation 
https://camel.apache.org/components/4.4.x/eips/saga-eip.html#_using_the_in_memory_saga_service

### Code Walkthrough 

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

When creating a new Inventory, bean `createInventoryService` is called (you can refer to CreateInventoryService.java)

Following actions are undertaken by SAGA: 
1. Positive Case: If new inventory is created, after execution of `direct:newInventory` "completion" route is called ie `direct:completeInventory`
2. Negative Case: If error is thrown while creating Inventory, "compensation" route is called ie `direct:cancelInventory`


# How to Run this Application 
This is a simple maven application, just clone the repo and open cmd and run:

To Compile: 
```
mvn clean install
```

To run the spring application
```
mvn clean compile exec:java
```

# How to Test the Application 
Open postman and hit this **GET** endpoint:
```
localhost:8006/createInventory
```
Note: By Default application run on **8006** port. Which can be changed under `application.yml` file

```
server:
  port: 8006
```

