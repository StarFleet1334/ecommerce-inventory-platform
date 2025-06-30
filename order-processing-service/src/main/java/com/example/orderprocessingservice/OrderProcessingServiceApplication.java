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

        1. Customer Exceptions
        2. Redis Caching
        3. Other Tables & Logics for it
        4. Location Distance calculator and time for is using a different type of transporters
        5. Time Logic to be applied to fields
        6 SpeedUp functionality to speed up order
        7. Email service to send email to customer on order receive on 15 minutes away and on door
        8. Introduce AI drivers that might pick up orders from the pool on their own
        9. Cleanup code
        10. Page of graphs about tables, like wareHouses, products, customers, employees to wareHouses etc.,


     */
}
