package com.example.orderprocessingservice.config.database.loaders;

import com.example.orderprocessingservice.config.database.AbstractDataLoader;
import com.example.orderprocessingservice.dto.messages.ProductMessage;
import com.example.orderprocessingservice.dto.model.asset.Product;
import com.example.orderprocessingservice.repository.asset.ProductRepository;
import com.example.orderprocessingservice.utils.constants.UrlConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class ProductLoader extends AbstractDataLoader<Product, ProductMessage> {

    @Autowired
    public ProductLoader(ObjectMapper objectMapper,
                         ProductRepository repository) {
        super(objectMapper,
                "data/products.json",
                UrlConstants.PRODUCT_POST_ENDPOINT,
                repository);
    }

    @Override
    protected TypeReference<List<Product>> getTypeReference() {
        return new TypeReference<>() {
        };
    }

    @Override
    protected ProductMessage convertToMessage(Product product) {
        ProductMessage productMessage = new ProductMessage();
        productMessage.setProduct_name(product.getProduct_name());
        productMessage.setProduct_description(product.getProduct_description());
        productMessage.setProduct_price(product.getProduct_price());
        productMessage.setProduct_id(productMessage.getProduct_id());
        productMessage.setSku(product.getSku());
        return productMessage;
    }

    @Override
    protected String getEntityIdentifier(Product entity) {
        return entity.getProduct_id();
    }
}
