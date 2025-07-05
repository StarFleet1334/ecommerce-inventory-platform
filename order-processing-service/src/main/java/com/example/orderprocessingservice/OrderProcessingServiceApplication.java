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
        6. Create some connection between WareHouse and Product (Refrigeration)
        7. If we cannot get from all wareHouses that product amount, then we just get whatever we can
        and move that order or implement some logic to move to a wait list, and when wareHouse gets that product,
        it automatically gets that again and goes like so.


     */
}
