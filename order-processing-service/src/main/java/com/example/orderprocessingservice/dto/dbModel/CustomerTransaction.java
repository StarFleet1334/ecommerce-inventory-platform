package com.example.orderprocessingservice.dto.dbModel;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "CustomerTransaction")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "expected_delivery_time", nullable = false)
    private OffsetDateTime expectedDeliveryTime;

    @Column(name = "is_finished")
    private boolean isFinished;
}
