package com.example.orderprocessingservice.config.database.loaders;

import com.example.orderprocessingservice.config.database.AbstractDataLoader;
import com.example.orderprocessingservice.dto.messages.EmployeeMessage;
import com.example.orderprocessingservice.dto.model.personnel.Employee;
import com.example.orderprocessingservice.dto.model.personnel.WareHouse;
import com.example.orderprocessingservice.repository.personnel.EmployeeRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.example.orderprocessingservice.utils.constants.UrlConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeLoader extends AbstractDataLoader<Employee, EmployeeMessage> {
    protected final WareHouseRepository wareHouseRepository;

    @Autowired
    public EmployeeLoader(ObjectMapper objectMapper,
                          EmployeeRepository repository, WareHouseRepository wareHouseRepository) {
        super(objectMapper,
                "data/employees.json",
                UrlConstants.EMPLOYEE_POST_ENDPOINT,
                repository);
        this.wareHouseRepository = wareHouseRepository;
    }

    @Override
    protected TypeReference<List<Employee>> getTypeReference() {
        return new TypeReference<>() {
        };
    }

    @Override
    protected void preProcessEntities(List<Employee> entities) {
        for (Employee emp : entities) {
            Integer warehouseId = emp.getWareHouseId();
            if (warehouseId == null) {
                throw new IllegalStateException("Missing wareHouseId for employee: " + emp.getEmail());
            }

            WareHouse wareHouse = wareHouseRepository.findById(warehouseId)
                    .orElseThrow(() -> new RuntimeException("WareHouse not found with id = " + warehouseId));
            emp.setWareHouse(wareHouse);
        }
    }

    @Override
    protected EmployeeMessage convertToMessage(Employee employee) {
        EmployeeMessage employeeMessage = new EmployeeMessage();
        employeeMessage.setFirst_name(employee.getFirstName());
        employeeMessage.setLast_name(employee.getLastName());
        employeeMessage.setEmail(employee.getEmail());
        employeeMessage.setPhone_number(employee.getPhoneNumber());
        employeeMessage.setWare_house_id(employee.getWareHouse().getWareHouseId());
        return employeeMessage;
    }

    @Override
    protected String getEntityIdentifier(Employee entity) {
        return entity.getEmail();
    }
}
