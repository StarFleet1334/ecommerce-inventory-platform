package com.example.orderprocessingservice.repository.personnel;

import com.example.orderprocessingservice.dto.model.personnel.WareHouseInventoryBacklog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WareHouseInventoryBacklogRepository extends JpaRepository<WareHouseInventoryBacklog, Integer> {

    @Query("SELECT w FROM WareHouseInventoryBacklog w WHERE w.wareHouse.wareHouseId = :wId AND w.product.product_id = :pId")
    List<WareHouseInventoryBacklog> findAllByWareHouseId_ProductId(@Param("wId") int wId, @Param("pId") String pId);
}
