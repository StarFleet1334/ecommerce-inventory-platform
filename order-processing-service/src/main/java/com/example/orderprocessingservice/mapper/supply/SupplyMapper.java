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
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class SupplyMapper implements BaseMapper<SupplyMP, Supply> {
    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected SupplierRepository supplierRepository;

    @Autowired
    protected EmployeeRepository employeeRepository;

    @Override
    @Mapping(source = "product_id", target = "product", qualifiedByName = "mapProduct")
    @Mapping(source = "supplier_id", target = "supplier", qualifiedByName = "mapSupplier")
    @Mapping(source = "employee_id", target = "employee", qualifiedByName = "mapEmployee")
    @Mapping(source = "supply_time", target = "supplyTime")
    @Mapping(source = "amount", target = "amount")
    @Mapping(target = "id", ignore = true)
    public abstract Supply toEntity(SupplyMP dto);

    @Named("mapProduct")
    protected Product mapProduct(String productId) {
        Product product = productRepository.findByProductId(productId);
        if (product == null) {
            throw ProductException.notFound(productId);
        }
        return product;
    }

    @Named("mapSupplier")
    protected Supplier mapSupplier(int supplierId) {
        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> SupplierException.notFound(supplierId));
    }

    @Named("mapEmployee")
    protected Employee mapEmployee(int employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> EmployeeException.notFound(employeeId));
    }
}
