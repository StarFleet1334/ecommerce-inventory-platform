package com.example.orderprocessingservice.mapper.supply;

import com.example.orderprocessingservice.dto.eventDto.SupplyMP;
import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.dto.model.asset.Supply;
import com.example.orderprocessingservice.dto.model.personnel.Employee;
import com.example.orderprocessingservice.dto.model.supplier.Supplier;
import com.example.orderprocessingservice.exception.asset.ProductException;
import com.example.orderprocessingservice.exception.personnel.EmployeeException;
import com.example.orderprocessingservice.exception.supplier.SupplierException;
import com.example.orderprocessingservice.mapper.base.BaseMapper;
import com.example.orderprocessingservice.repository.asset.ProductRepository;
import com.example.orderprocessingservice.repository.personnel.EmployeeRepository;
import com.example.orderprocessingservice.repository.supplier.SupplierRepository;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public abstract class SupplyMapper implements BaseMapper<SupplyMP, Supply> {

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected EmployeeRepository employeeRepository;

    @Autowired
    protected SupplierRepository supplierRepository;

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "supplier_id", target = "supplier", qualifiedByName = "mapSupplier")
    @Mapping(source = "product_id", target = "product", qualifiedByName = "mapProduct")
    @Mapping(source = "employee_id", target = "employee", qualifiedByName = "mapEmployee")
    @Mapping(source = "supply_time", target = "supplyTime")
    @Mapping(source = "amount", target = "amount")
    public abstract Supply toEntity(SupplyMP dto);

    @Named("mapSupplier")
    protected Supplier mapSupplier(int supplier_id) {
        Optional<Supplier> supplier = supplierRepository.findById(supplier_id);
        if (supplier.isEmpty()) {
            throw SupplierException.notFound(supplier_id);
        }
        return supplier.get();
    }

    @Named("mapProduct")
    protected Product mapProduct(String product_id) {
        Product product = productRepository.findByProductId(product_id);
        if (product == null) {
            throw ProductException.notFound(product_id);
        }
        return product;
    }

    @Named("mapEmployee")
    protected Employee mapEmployee(int employee_id) {
        Optional<Employee> employee = employeeRepository.findById(employee_id);
        if (employee.isEmpty()) {
            throw EmployeeException.notFound(employee_id);
        }
        return employee.get();
    }



}
