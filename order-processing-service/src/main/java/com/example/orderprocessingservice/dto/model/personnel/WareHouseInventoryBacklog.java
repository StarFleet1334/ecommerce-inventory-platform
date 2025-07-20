package com.example.orderprocessingservice.dto.model.personnel;

import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.dto.model.order.CustomerOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "warehouse_inventory_backlog")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WareHouseInventoryBacklog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "ware_house_id", referencedColumnName = "ware_house_id")
    private WareHouse wareHouse;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private CustomerOrder customerOrder;

    @Column(name = "debt_quantity")
    @JsonProperty("debt_quantity")
    private Integer debtQuantity;

    @Column(name = "recorded_at")
    @JsonProperty("recorded_at")
    private OffsetDateTime recordedAt;

    @Column(name = "last_updated_at")
    @JsonProperty("last_updated_at")
    private OffsetDateTime lastUpdatedAt;

//    @Transient
//    @JsonIgnore
//    private Integer wareHouseId;
//
//    @JsonProperty("ware_house_id")
//    private void setWareHouseId(Integer id) {
//        this.wareHouseId = id;
//    }
//
//    @JsonProperty("ware_house_id")
//    public Integer getWareHouseIdForJson() {
//        return wareHouse != null ? wareHouse.getWareHouseId() : null;
//    }
//
//    @Transient
//    @JsonIgnore
//    private String productId;
//
//    @JsonProperty("product_id")
//    private void setProductId(String id) {
//        this.productId = id;
//    }
//
//    @JsonProperty("product_id")
//    public String getProductIdForJson() {
//        return product != null ? product.getProduct_id() : null;
//    }
//
//
//    @Transient
//    @JsonIgnore
//    private Integer orderId;
//
//    @JsonProperty("order_id")
//    private void setOrderId(Integer id) {
//        this.orderId = id;
//    }
//
//    @JsonProperty("order_id")
//    public Integer getOrderIdForJson() {
//        return customerOrder != null ? customerOrder.getOrderId() : null;
//    }
}
