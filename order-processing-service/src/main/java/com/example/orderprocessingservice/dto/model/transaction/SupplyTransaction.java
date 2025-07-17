package com.example.orderprocessingservice.dto.model.transaction;

import com.example.orderprocessingservice.dto.model.asset.Supply;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "supply_transaction")
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
    private OffsetDateTime expected_delivery_time;

    @Column(name = "is_delivered")
    @JsonProperty("is_delivered")
    private boolean delivered;

}
