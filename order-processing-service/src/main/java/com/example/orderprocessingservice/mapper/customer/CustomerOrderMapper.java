package com.example.orderprocessingservice.mapper.customer;

import com.example.orderprocessingservice.dto.eventDto.CustomerOrderMP;
import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.dto.model.customer.Customer;
import com.example.orderprocessingservice.dto.model.order.CustomerOrder;
import com.example.orderprocessingservice.exception.asset.ProductException;
import com.example.orderprocessingservice.exception.customer.CustomerException;
import com.example.orderprocessingservice.mapper.base.BaseMapper;
import com.example.orderprocessingservice.repository.asset.ProductRepository;
import com.example.orderprocessingservice.repository.customer.CustomerRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

@Mapper(componentModel = "spring", uses = {ProductRepository.class, CustomerRepository.class})
public abstract class CustomerOrderMapper implements BaseMapper<CustomerOrderMP, CustomerOrder> {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerOrderMapper(ProductRepository productRepository, CustomerRepository customerRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Mapping(source = "product_id",target = "product",qualifiedByName = "mapProduct")
    @Mapping(source = "product_amount",target = "productAmount")
    @Mapping(source = "order_time",target = "orderTime")
    @Mapping(source = "customer_id",target = "customer",qualifiedByName = "mapCustomer")
    @Mapping(target = "orderId",ignore = true)
    public abstract CustomerOrder toEntity(CustomerOrderMP dto);

    @Named("mapProduct")
    protected Product mapProduct(String product_id) {
        Product product = productRepository.findByProductId(product_id);
        if (product == null) {
            throw ProductException.notFound(product_id);
        }
        return product;
    }

    @Named("mapCustomer")
    protected Customer mapCustomer(int customer_id) {
        Optional<Customer> customer = customerRepository.findById(customer_id);
        if (customer.isEmpty()) {
            throw CustomerException.notFound(customer_id);
        }
        return customer.get();
    }

}
