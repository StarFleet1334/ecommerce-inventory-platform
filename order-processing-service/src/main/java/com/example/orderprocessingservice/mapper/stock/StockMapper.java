package com.example.orderprocessingservice.mapper.stock;

import com.example.orderprocessingservice.dto.eventDto.StockMP;
import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.dto.model.asset.Stock;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.mapper.base.BaseMapper;
import com.example.orderprocessingservice.repository.asset.ProductRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class StockMapper implements BaseMapper<StockMP, Stock> {

    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected WareHouseRepository wareHouseRepository;

    @Override
    @Mapping(source = "ware_house_id", target = "wareHouse", qualifiedByName = "mapWareHouse")
    @Mapping(source = "product_id", target = "product", qualifiedByName = "mapProduct")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(target = "id", ignore = true)
    public abstract Stock toEntity(StockMP dto);

    @Named("mapWareHouse")
    protected WareHouse mapWareHouse(int wareHouseId) {
        return wareHouseRepository.findById(wareHouseId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("WareHouse not found with id: %d", wareHouseId)));
    }

    @Named("mapProduct")
    protected Product mapProduct(String productId) {
        Product product = productRepository.findByProductId(productId);
        if (product == null) {
            throw new IllegalArgumentException(
                    String.format("Product not found with id: %s", productId));
        }
        return product;
    }
}
