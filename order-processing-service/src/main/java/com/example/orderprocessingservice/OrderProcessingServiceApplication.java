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
        2. Other Tables & Logics for it
        3. Location Distance calculator and time for are using a different type of transporters
        4. Time Logic to be applied to fields
        5 SpeedUp functionalities to speed up order
        6. Email service to send email to customer on order receive on 15 minutes away and on door
        7. Introduce AI drivers that might pick up orders from the pool on their own
        8. Cleanup code
        9. Page of graphs about tables, like wareHouses, products, customers, employees to wareHouses, etc.,

     */
}
