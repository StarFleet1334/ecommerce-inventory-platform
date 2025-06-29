package com.example.orderprocessingservice.validator;

import com.example.orderprocessingservice.dto.mapped.EmployeeMP;
import com.example.orderprocessingservice.repository.personnel.EmployeeRepository;
import com.example.orderprocessingservice.utils.constants.EmployeeConstants;
import com.example.orderprocessingservice.validator.base.BaseEntityValidator;
import com.example.orderprocessingservice.validator.base.ValidateRule;
import org.springframework.stereotype.Component;

@Component
public class EmployeeValidator extends BaseEntityValidator<EmployeeMP> {
    private final EmployeeRepository employeeRepository;

    public EmployeeValidator(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;

        rules.add(ValidateRule.forField("first_name",EmployeeMP::getFirst_name)
                .required()
                .maxLength(EmployeeConstants.MAX_FIRST_NAME_LENGTH));

        rules.add(ValidateRule.forField("last_name",EmployeeMP::getLast_name)
                .required()
                .maxLength(EmployeeConstants.MAX_LAST_NAME_LENGTH));

        rules.add(ValidateRule.forField("email",EmployeeMP::getEmail)
                .required()
                .maxLength(EmployeeConstants.MAX_EMAIL_LENGTH)
                .unique(email -> employeeRepository.existsByEmail((String) email)));

        rules.add(ValidateRule.forField("phone_number",EmployeeMP::getPhone_number)
                .required()
                .maxLength(EmployeeConstants.MAX_PHONE_NUMBER_LENGTH)
                .unique(phoneNumber -> employeeRepository.existsByPhoneNumber((String) phoneNumber)));
    }
}
