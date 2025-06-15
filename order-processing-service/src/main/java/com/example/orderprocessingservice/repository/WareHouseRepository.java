package com.example.orderprocessingservice.repository;

import com.example.orderprocessingservice.dto.dbModel.WareHouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WareHouseRepository extends JpaRepository<WareHouse, Integer> {
}
