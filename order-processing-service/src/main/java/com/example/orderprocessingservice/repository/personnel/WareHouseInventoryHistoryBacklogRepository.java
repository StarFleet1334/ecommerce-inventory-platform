package com.example.orderprocessingservice.repository.personnel;

import com.example.orderprocessingservice.dto.model.personnel.WareHouseInventoryBacklogHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WareHouseInventoryHistoryBacklogRepository extends JpaRepository<WareHouseInventoryBacklogHistory,Integer> {
}
