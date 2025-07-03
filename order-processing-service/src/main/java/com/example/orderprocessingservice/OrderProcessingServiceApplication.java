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
        3. Email service to send email to customer on order receive on 15 minutes away and on door
        4. Introduce AI drivers that might pick up orders from the pool on their own
        5. Cleanup code
        6. Page of graphs about tables, like wareHouses, products, customers, employees to wareHouses, etc.,
        7. Add functionality such that, when a customer orders something and that is not available that amount
        for now, this order can go to a wait list and when a new order comes, it will automatically send information
        that it is available, maybe on email

     */
}
