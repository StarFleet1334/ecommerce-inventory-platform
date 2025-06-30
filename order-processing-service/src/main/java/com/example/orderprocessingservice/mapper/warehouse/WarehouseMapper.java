package com.example.orderprocessingservice.mapper.warehouse;

import com.example.orderprocessingservice.dto.eventDto.WareHouseMP;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WarehouseMapper extends BaseMapper<WareHouseMP, WareHouse> {

    @Override
    @Mapping(source = "ware_house_name",target = "wareHouseName")
    @Mapping(source = "refrigerated", target = "refrigerated")
    @Mapping(target = "wareHouseCapacity", constant = "0")
    @Mapping(source = "min_stock_level",target = "minStockLevel")
    @Mapping(source = "max_stock_level",target = "maxStockLevel")
    @Mapping(source = "latitude",target = "latitude")
    @Mapping(source = "longitude",target = "longitude")
    @Mapping(target = "employeeList", ignore = true)
    @Mapping(target = "wareHouseId", ignore = true)
    WareHouse toEntity(WareHouseMP dto);
}
