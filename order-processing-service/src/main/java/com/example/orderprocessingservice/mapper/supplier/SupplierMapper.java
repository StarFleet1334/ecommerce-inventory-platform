package com.example.orderprocessingservice.mapper.supplier;

import com.example.orderprocessingservice.dto.mapped.SupplierMP;
import com.example.orderprocessingservice.dto.model.supplier.Supplier;
import com.example.orderprocessingservice.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SupplierMapper extends BaseMapper<SupplierMP, Supplier> {

    @Override
    @Mapping(source = "first_name",target = "firstName")
    @Mapping(source = "last_name",target = "lastName")
    @Mapping(source = "email",target = "email")
    @Mapping(source = "phone_number",target = "phoneNumber")
    @Mapping(source = "latitude",target = "latitude")
    @Mapping(source = "longitude",target = "longitude")
    @Mapping(target = "supplier_id", ignore = true)
    Supplier toEntity(SupplierMP dto);
}
