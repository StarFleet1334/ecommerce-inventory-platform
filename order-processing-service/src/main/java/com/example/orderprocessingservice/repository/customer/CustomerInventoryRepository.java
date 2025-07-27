package com.example.orderprocessingservice.repository.customer;

import com.example.orderprocessingservice.dto.model.customer.CustomerInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface CustomerInventoryRepository extends JpaRepository<CustomerInventory, Integer> {
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM CustomerInventory s WHERE s.customerId = :customerId AND s.productId = :productId")
    boolean existsByCustomerIdAndProductId(@Param("customerId") Integer customerId, @Param("productId") String productId);

    Optional<CustomerInventory> findByCustomerIdAndProductId(Integer customerId, String productId);

    List<CustomerInventory> findAllByCustomerId(Integer customerId);

    @Modifying
    @Query(value = """
        INSERT INTO customer_inventory
              (customer_id, product_id, quantity, last_updated)
        VALUES (:customerId, :productId, :delta, :ts)
        ON CONFLICT (customer_id, product_id)
        DO UPDATE
        SET quantity     = customer_inventory.quantity + :delta,
            last_updated = :ts
        """, nativeQuery = true)
    void upsertQuantity(@Param("customerId") int customerId,
                        @Param("productId")  String productId,
                        @Param("delta")      int delta,
                        @Param("ts") OffsetDateTime ts);
}
