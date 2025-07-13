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
        5. If we cannot get from all wareHouses that product amount, then we just get whatever we can
        and move that order or implement some logic to move to a wait list, and when wareHouse gets that product,
        it automatically gets that again and goes like so.
        6. We should send to "graphs" customer, employee, product, supplier, warehouse tables for now.
        first when we load to database tables with data, we should send that as init data and then on each call

        7. Also, when we delete WareHouse by id, all affiliated stocks must also be deleted

     */
}
