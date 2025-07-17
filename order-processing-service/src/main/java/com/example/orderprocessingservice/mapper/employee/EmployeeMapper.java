package com.example.orderprocessingservice.mapper.employee;

import com.example.orderprocessingservice.dto.eventDto.EmployeeMP;
import com.example.orderprocessingservice.dto.model.personnel.Employee;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.exception.personnel.WareHouseException;
import com.example.orderprocessingservice.mapper.base.BaseMapper;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

@Mapper(componentModel = "spring",uses = {WareHouseRepository.class})
public abstract class EmployeeMapper implements BaseMapper<EmployeeMP, Employee> {

    private final WareHouseRepository wareHouseRepository;

    @Autowired
    public EmployeeMapper(WareHouseRepository wareHouseRepository) {
        this.wareHouseRepository = wareHouseRepository;
    }

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
        Optional<WareHouse> wareHouse = wareHouseRepository.findById(wareHouseId);
        if (wareHouse.isEmpty()) {
            throw WareHouseException.notFound(wareHouseId);
        }
        return wareHouse.get();
    }

}

