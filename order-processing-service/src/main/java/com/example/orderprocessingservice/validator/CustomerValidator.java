package com.example.orderprocessingservice.validator;

import com.example.orderprocessingservice.dto.mapped.CustomerMP;
import com.example.orderprocessingservice.repository.customer.CustomerRepository;
import com.example.orderprocessingservice.utils.constants.CustomerConstants;
import com.example.orderprocessingservice.validator.base.BaseEntityValidator;
import com.example.orderprocessingservice.validator.base.ValidateRule;
import org.springframework.stereotype.Component;

@Component
public class CustomerValidator extends BaseEntityValidator<CustomerMP> {
    private final CustomerRepository customerRepository;

    public CustomerValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;

        rules.add(ValidateRule.forField("first_name",CustomerMP::getFirst_name)
                .required()
                .maxLength(CustomerConstants.MAX_FIRST_NAME_LENGTH));

        rules.add(ValidateRule.forField("last_name",CustomerMP::getLast_name)
                .required()
                .maxLength(CustomerConstants.MAX_LAST_NAME_LENGTH));

        rules.add(ValidateRule.forField("email",CustomerMP::getEmail)
                .required()
                .maxLength(CustomerConstants.MAX_EMAIL_LENGTH)
                .unique(email -> customerRepository.existsByEmail((String) email)));

        rules.add(ValidateRule.forField("phone_number",CustomerMP::getPhone_number)
                .required()
                .maxLength(CustomerConstants.MAX_PHONE_NUMBER_LENGTH)
                .unique(phoneNumber -> customerRepository.existsByPhoneNumber((String) phoneNumber)));

        rules.add(ValidateRule.forField("latitude", CustomerMP::getLatitude)
                .required());

        rules.add(ValidateRule.forField("longitude", CustomerMP::getLongitude)
                .required());

    }
}
