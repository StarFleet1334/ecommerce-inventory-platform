package com.example.orderprocessingservice.validator;

import com.example.orderprocessingservice.dto.eventDto.CustomerOrderMP;
import com.example.orderprocessingservice.repository.customer.CustomerOrderRepository;
import com.example.orderprocessingservice.validator.base.BaseEntityValidator;
import com.example.orderprocessingservice.validator.base.ValidateRule;
import org.springframework.stereotype.Component;

@Component
public class CustomerOrderValidator extends BaseEntityValidator<CustomerOrderMP> {
    private final CustomerOrderRepository customerOrderRepository;

    public CustomerOrderValidator(CustomerOrderRepository customerOrderRepository) {
        this.customerOrderRepository = customerOrderRepository;

        rules.add(ValidateRule.forField("product_id",CustomerOrderMP::getProduct_id)
                .required());

        rules.add(ValidateRule.forField("order_time",CustomerOrderMP::getOrder_time)
                .required());

        rules.add(ValidateRule.forField("customer_id",CustomerOrderMP::getCustomer_id)
                .required());
    }
}
