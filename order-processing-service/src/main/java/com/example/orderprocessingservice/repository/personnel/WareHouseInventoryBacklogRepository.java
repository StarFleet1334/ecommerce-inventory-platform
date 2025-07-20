package com.example.orderprocessingservice.repository.personnel;

import com.example.orderprocessingservice.dto.model.personnel.WareHouseInventoryBacklog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WareHouseInventoryBacklogRepository extends JpaRepository<WareHouseInventoryBacklog, Integer> {
}
