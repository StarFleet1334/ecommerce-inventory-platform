package com.example.orderprocessingservice.repository;

import com.example.orderprocessingservice.dto.dbModel.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface ProductRepository extends JpaRepository<Product, BigInteger> {
    @Query("SELECT p FROM Product p WHERE p.product_id = :product_id")
    Product findByProductId(@Param("product_id") String product_id);

}
