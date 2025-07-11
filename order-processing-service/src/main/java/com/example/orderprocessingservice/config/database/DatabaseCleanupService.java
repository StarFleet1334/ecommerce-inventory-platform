package com.example.orderprocessingservice.config.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DatabaseCleanupService {
    @PersistenceContext
    private EntityManager entityManager;

    public void truncateWareHouseTables() {
        entityManager.createNativeQuery("TRUNCATE ware_house RESTART IDENTITY CASCADE")
                .executeUpdate();
    }

    public void truncateProductsTables() {
        entityManager.createNativeQuery("TRUNCATE product RESTART IDENTITY CASCADE")
                .executeUpdate();
    }

    public void truncateCustomersTables() {
        entityManager.createNativeQuery("TRUNCATE customer RESTART IDENTITY CASCADE")
                .executeUpdate();
    }

    public void truncateSupplierTables() {
        entityManager.createNativeQuery("TRUNCATE supplier RESTART IDENTITY CASCADE")
                .executeUpdate();
    }

    public void truncateEmployeeTables() {
        entityManager.createNativeQuery("TRUNCATE employee RESTART IDENTITY CASCADE")
                .executeUpdate();
    }

    public void truncateStockTables() {
        entityManager.createNativeQuery("TRUNCATE stock RESTART IDENTITY CASCADE")
                .executeUpdate();
    }
}
