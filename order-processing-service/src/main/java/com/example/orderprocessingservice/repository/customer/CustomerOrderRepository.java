package com.example.orderprocessingservice.repository.customer;

import com.example.orderprocessingservice.dto.model.order.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Integer> {
    @Query("select co from CustomerOrder co where co.customer.customer_id = :cid")
    List<CustomerOrder> findByCustomerId(@Param("cid") Integer cid);
}
