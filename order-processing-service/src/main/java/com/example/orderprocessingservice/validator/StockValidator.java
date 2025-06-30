package com.example.orderprocessingservice.validator;

import com.example.orderprocessingservice.dto.eventDto.StockMP;
import com.example.orderprocessingservice.repository.asset.ProductRepository;
import com.example.orderprocessingservice.repository.asset.StockRepository;
import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import com.example.orderprocessingservice.utils.constants.StockConstants;
import com.example.orderprocessingservice.validator.base.BaseEntityValidator;
import com.example.orderprocessingservice.validator.base.ValidateRule;
import org.springframework.stereotype.Component;

@Component
public class StockValidator extends BaseEntityValidator<StockMP> {
    private final StockRepository stockRepository;
    private final WareHouseRepository wareHouseRepository;
    private final ProductRepository productRepository;

    public StockValidator(StockRepository stockRepository, WareHouseRepository wareHouseRepository, ProductRepository productRepository) {
        this.stockRepository = stockRepository;
        this.wareHouseRepository = wareHouseRepository;
        this.productRepository = productRepository;

        rules.add(ValidateRule.forField("ware_house_id", StockMP::getWare_house_id)
                .required()
                .unique(id -> !wareHouseRepository.existsById((Integer) id)));

        rules.add(ValidateRule.forField("product_id", StockMP::getProduct_id)
                .required()
                .maxLength(StockConstants.MAX_PRODUCT_ID_LENGTH)
                .unique(id -> !productRepository.existsByProductId((String) id)));

        rules.add(ValidateRule.forField("quantity", StockMP::getQuantity)
                .required()
                .custom(quantity -> quantity instanceof Integer && (Integer) quantity >= 0, "Quantity must be a non-negative integer"));
    }
}
