package com.example.orderprocessingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderProcessingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderProcessingServiceApplication.class, args);
    }


    /*
                TASKS:
        1. Redis Caching
        2. Email service to send email to customer on order receive on 15 minutes away and on door
        3. Introduce AI drivers that might pick up orders from the pool on their own
        4. Cleanup code
        5. Page of graphs about tables, like wareHouses, products, customers, employees to wareHouses, etc.,
        6. Add functionality such that, when a customer orders something and that is not available that amount
        for now, this order can go to a wait list and when a new order comes, it will automatically send information
        that it is available, maybe on email
        7. Also, when we delete order, transaction should also delete, Same thing when we delete supply, but the thing is
        we need another database like MongoDb, and we will move moved transactions and orders combined there as a single entry.
        It will be like archive
        8. Create some connection between WareHouse and Product (Refrigeration)

     */
}
