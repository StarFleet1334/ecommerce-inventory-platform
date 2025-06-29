package com.example.orderprocessingservice.validator;

import com.example.orderprocessingservice.dto.mapped.ProductMP;
import com.example.orderprocessingservice.repository.asset.ProductRepository;
import com.example.orderprocessingservice.utils.constants.ProductConstants;
import com.example.orderprocessingservice.validator.base.BaseEntityValidator;
import com.example.orderprocessingservice.validator.base.ValidateRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductValidator extends BaseEntityValidator<ProductMP> {
    private final ProductRepository productRepository;

    public ProductValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;

        rules.add(ValidateRule.forField("product_name",ProductMP::getProduct_name)
                .required()
                .maxLength(ProductConstants.MAX_PRODUCT_NAME_LENGTH));

        rules.add(ValidateRule.forField("sku",ProductMP::getSku)
                .required()
                .maxLength(ProductConstants.MAX_PRODUCT_SKU_LENGTH));

        rules.add(ValidateRule.forField("product_id",ProductMP::getProduct_id)
                .required()
                .maxLength(ProductConstants.MAX_PRODUCT_ID_LENGTH)
                .unique(id -> productRepository.existsByProductId((String) id)));

        rules.add(ValidateRule.forField("product_description",ProductMP::getProduct_description)
                .required()
                .maxLength(ProductConstants.MAX_PRODUCT_DESCRIPTION_LENGTH));


        rules.add(ValidateRule.forField("product_price", ProductMP::getProduct_price)
                .required()
                .custom(price -> {
                    if (price == null) return true;
                    BigDecimal bdPrice = (BigDecimal) price;
                    String[] parts = bdPrice.stripTrailingZeros().toPlainString().split("\\.");
                    boolean validIntegerPart = parts[0].length() <= ProductConstants.MAX_PRODUCT_PRICE_DIGITS_INTEGER;
                    boolean validDecimalPart = parts.length == 1 || parts[1].length() <= ProductConstants.MAX_PRODUCT_PRICE_DIGITS_FRACTIONAL;
                    return validIntegerPart && validDecimalPart && bdPrice.compareTo(BigDecimal.ZERO) > 0;
                }, "Product price must be positive and have at most 3 digits before decimal and 1 digit after decimal point"));

    }
}
