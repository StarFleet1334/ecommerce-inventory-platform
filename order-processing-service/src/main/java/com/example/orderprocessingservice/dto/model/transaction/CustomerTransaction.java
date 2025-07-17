package com.example.orderprocessingservice.dto.model.transaction;

import com.example.orderprocessingservice.dto.model.order.CustomerOrder;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "customer_transaction")
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
    private CustomerOrder customerOrder;

    @Column(name = "expected_delivery_time", nullable = false)
    private OffsetDateTime expected_delivery_time;

    @Column(name = "is_finished")
    @JsonProperty("is_finished")
    private boolean finished;

}
