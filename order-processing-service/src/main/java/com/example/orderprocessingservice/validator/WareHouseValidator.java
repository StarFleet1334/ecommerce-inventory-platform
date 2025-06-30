package com.example.orderprocessingservice.validator;

import com.example.orderprocessingservice.dto.eventDto.WareHouseMP;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.example.orderprocessingservice.utils.constants.WareHouseConstants;
import com.example.orderprocessingservice.validator.base.BaseEntityValidator;
import com.example.orderprocessingservice.validator.base.ValidateRule;
import org.springframework.stereotype.Component;


@Component
public class WareHouseValidator extends BaseEntityValidator<WareHouseMP> {
    private final WareHouseRepository wareHouseRepository;

    public WareHouseValidator(WareHouseRepository wareHouseRepository) {
        this.wareHouseRepository = wareHouseRepository;

        rules.add(ValidateRule.forField("ware_house_name", WareHouseMP::getWare_house_name)
                .required()
                .maxLength(WareHouseConstants.WAREHOUSE_NAME_LENGTH)
                .unique(wareHouseName -> wareHouseRepository.existsByWareHouseName((String) wareHouseName)));

        rules.add(ValidateRule.forField("min_stock_level", WareHouseMP::getMin_stock_level)
                .required()
                .minValue(0)
                .dependentField("max_stock_level",
                        WareHouseMP::getMax_stock_level,
                        (min, max) -> ((Integer) min) <= ((Integer) max),
                        "Minimum stock level must be less than or equal to maximum stock level"));

        rules.add(ValidateRule.forField("max_stock_level", WareHouseMP::getMax_stock_level)
                .required()
                .minValue(0));

        rules.add(ValidateRule.forField("latitude", WareHouseMP::getLatitude)
                .required());

        rules.add(ValidateRule.forField("longitude", WareHouseMP::getLongitude)
                .required());

    }
}
