package com.example.orderprocessingservice.mapper.employee;

import com.example.orderprocessingservice.dto.mapped.EmployeeMP;
import com.example.orderprocessingservice.dto.model.personnel.Employee;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.mapper.base.BaseMapper;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class EmployeeMapper implements BaseMapper<EmployeeMP, Employee> {

    @Autowired
    protected WareHouseRepository wareHouseRepository;

    @Override
    @Mapping(source = "first_name", target = "firstName")
    @Mapping(source = "last_name", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone_number", target = "phoneNumber")
    @Mapping(source = "ware_house_id", target = "wareHouse", qualifiedByName = "mapWareHouse")
    @Mapping(target = "employee_id", ignore = true)
    @Mapping(target = "wareHouseId", ignore = true)
    public abstract Employee toEntity(EmployeeMP dto);

    @Named("mapWareHouse")
    protected WareHouse mapWareHouse(int wareHouseId) {
        return wareHouseRepository.findById(wareHouseId)
                .orElseThrow(() -> new IllegalArgumentException("WareHouse not found with id: " + wareHouseId));
    }
}

