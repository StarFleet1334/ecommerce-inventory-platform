package com.example.orderprocessingservice.validator;

import com.example.orderprocessingservice.dto.eventDto.SupplierMP;
import com.example.orderprocessingservice.repository.supplier.SupplierRepository;
import com.example.orderprocessingservice.utils.constants.SupplierConstants;
import com.example.orderprocessingservice.validator.base.BaseEntityValidator;
import com.example.orderprocessingservice.validator.base.ValidateRule;
import org.springframework.stereotype.Component;

@Component
public class SupplierValidator extends BaseEntityValidator<SupplierMP> {
    private final SupplierRepository supplierRepository;

    public SupplierValidator(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;

        rules.add(ValidateRule.forField("first_name",SupplierMP::getFirst_name)
                .required()
                .maxLength(SupplierConstants.MAX_FIRST_NAME_LENGTH));

        rules.add(ValidateRule.forField("last_name",SupplierMP::getLast_name)
                .required()
                .maxLength(SupplierConstants.MAX_LAST_NAME_LENGTH));

        rules.add(ValidateRule.forField("email",SupplierMP::getEmail)
                .required()
                .maxLength(SupplierConstants.MAX_EMAIL_LENGTH)
                .unique(email -> supplierRepository.existsByEmail((String) email)));

        rules.add(ValidateRule.forField("phone_number",SupplierMP::getPhone_number)
                .required()
                .maxLength(SupplierConstants.MAX_PHONE_NUMBER_LENGTH)
                .unique(phoneNumber -> supplierRepository.existsByPhoneNumber((String) phoneNumber)));

        rules.add(ValidateRule.forField("latitude", SupplierMP::getLatitude)
                .required());

        rules.add(ValidateRule.forField("longitude", SupplierMP::getLongitude)
                .required());
    }
}
