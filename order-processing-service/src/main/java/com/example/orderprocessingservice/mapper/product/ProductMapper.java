package com.example.orderprocessingservice.mapper.product;

import com.example.orderprocessingservice.dto.mapped.ProductMP;
import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper extends BaseMapper<ProductMP, Product> {

    @Override
    @Mapping(source = "product_name",target = "product_name")
    @Mapping(source = "sku",target = "sku")
    @Mapping(source = "product_id",target = "product_id")
    @Mapping(source = "product_price",target = "product_price")
    @Mapping(source = "product_description",target = "product_description")
    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductMP dto);
}
