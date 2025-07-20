package com.example.orderprocessingservice.dto.model.personnel;

import com.example.orderprocessingservice.dto.model.order.CustomerOrder;
import com.example.orderprocessingservice.utils.constants.ProductConstants;
import com.example.orderprocessingservice.utils.constants.WareHouseConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "warehouse_inventory_backlog_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WareHouseInventoryBacklogHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "ware_house_name",length = WareHouseConstants.WAREHOUSE_NAME_LENGTH)
    @Size(max = WareHouseConstants.WAREHOUSE_NAME_LENGTH)
    @JsonProperty("ware_house_name")
    private String wareHouseName;

    @Column(name = "product_name",length = ProductConstants.MAX_PRODUCT_NAME_LENGTH)
    @Size(max = ProductConstants.MAX_PRODUCT_NAME_LENGTH)
    @JsonProperty("product_name")
    private String productName;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private CustomerOrder customerOrder;

    @Column(name = "debt_quantity")
    @JsonProperty("debt_quantity")
    private String debtQuantity;

    @Column(name = "recorded_at")
    @JsonProperty("recorded_at")
    private OffsetDateTime recordedAt;

    @Column(name = "last_updated_at")
    @JsonProperty("last_updated_at")
    private OffsetDateTime lastUpdatedAt;

}
