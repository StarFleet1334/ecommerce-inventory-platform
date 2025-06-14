package com.example.orderprocessingservice.dto.dbModel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "SupplyTransaction")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplyTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "supply_id", nullable = false)
    private Supply supply;

    @Column(name = "expected_delivery_time")
    private OffsetDateTime expectedDeliveryTime;

    @Column(name = "isDelivered")
    private boolean isDelivered;

}
