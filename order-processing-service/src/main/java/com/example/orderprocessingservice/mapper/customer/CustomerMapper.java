package com.example.orderprocessingservice.mapper.customer;

import com.example.orderprocessingservice.dto.eventDto.CustomerMP;
import com.example.orderprocessingservice.dto.model.customer.Customer;
import com.example.orderprocessingservice.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper extends BaseMapper<CustomerMP, Customer> {
    @Override
    @Mapping(source = "first_name",target = "firstName")
    @Mapping(source = "last_name",target = "lastName")
    @Mapping(source = "email",target = "email")
    @Mapping(source = "phone_number",target = "phoneNumber")
    @Mapping(source = "latitude",target = "latitude")
    @Mapping(source = "longitude",target = "longitude")
    @Mapping(target = "customer_id", ignore = true)
    Customer toEntity(CustomerMP dto);
}
