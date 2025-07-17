package com.example.orderprocessingservice.repository.customer;

import com.example.orderprocessingservice.dto.model.customer.CustomerInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerInventoryRepository extends JpaRepository<CustomerInventory, Integer> {
    boolean existsByCustomerIdAndProductId(Integer customerId, String productId);

    Optional<CustomerInventory> findByCustomerIdAndProductId(Integer customerId, String productId);

}
