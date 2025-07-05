package com.example.orderprocessingservice.repository.transaction;

import com.example.orderprocessingservice.dto.model.transaction.SupplyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplyTransactionRepository extends JpaRepository<SupplyTransaction, Integer> {

    SupplyTransaction findBySupply_Id(int supplyId);

}
