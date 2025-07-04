package com.example.orderprocessingservice.repository.asset;

import com.example.orderprocessingservice.dto.model.asset.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Integer> {
    @Query("SELECT s FROM Stock s WHERE s.product.product_id = :product_id")
    List<Stock> findAllByProductId(@Param("product_id") String product_id);

    @Query("SELECT s FROM Stock s WHERE s.product.product_id = :product_id")
    Stock findByProductId(@Param("product_id") String product_id);

    @Modifying
    @Query("DELETE FROM Stock s WHERE s.product.product_id = :product_id")
    void deleteAllByProductId(@Param("product_id") String product_id);

    @Query("SELECT s FROM Stock s WHERE s.product.product_id = :productId AND s.wareHouse.wareHouseId = :wareHouseId")
    Stock findByProductIdAndWareHouseId(@Param("productId") String productId, @Param("wareHouseId") int wareHouseId);
}
