package com.example.orderprocessingservice.repository.personnel;

import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WareHouseRepository extends JpaRepository<WareHouse, Integer> {
    boolean existsByWareHouseName(String wareHouseName);
}
