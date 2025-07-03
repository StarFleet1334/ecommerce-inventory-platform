package com.example.orderprocessingservice.validator;


import com.example.orderprocessingservice.dto.eventDto.SupplyMP;
import com.example.orderprocessingservice.repository.supplier.SupplierRepository;
import com.example.orderprocessingservice.utils.constants.SupplyConstants;
import com.example.orderprocessingservice.validator.base.BaseEntityValidator;
import com.example.orderprocessingservice.validator.base.ValidateRule;
import org.springframework.stereotype.Component;

@Component
public class SupplyValidator  extends BaseEntityValidator<SupplyMP> {
    private final SupplierRepository supplierRepository;

    public SupplyValidator(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;

        rules.add(ValidateRule.forField("supplier_id",SupplyMP::getSupplier_id)
                .required());

        rules.add(ValidateRule.forField("product_id",SupplyMP::getProduct_id)
                .required()
                .maxLength(SupplyConstants.MAX_PRODUCT_ID_LENGTH));

        rules.add(ValidateRule.forField("employee_id",SupplyMP::getEmployee_id)
                .required());

        rules.add(ValidateRule.forField("supply_time",SupplyMP::getSupply_time)
                .required());

        rules.add(ValidateRule.forField("amount",SupplyMP::getAmount)
                .required());
    }
}
