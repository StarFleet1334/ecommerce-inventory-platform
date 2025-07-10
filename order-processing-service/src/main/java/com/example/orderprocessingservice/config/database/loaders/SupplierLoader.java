package com.example.orderprocessingservice.config.database.loaders;

import com.example.orderprocessingservice.config.database.AbstractDataLoader;
import com.example.orderprocessingservice.dto.messages.SupplierMessage;
import com.example.orderprocessingservice.dto.model.supplier.Supplier;
import com.example.orderprocessingservice.repository.supplier.SupplierRepository;
import com.example.orderprocessingservice.utils.constants.UrlConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SupplierLoader extends AbstractDataLoader<Supplier, SupplierMessage> {

    @Autowired
    public SupplierLoader(ObjectMapper objectMapper,
                          SupplierRepository repository) {
        super(objectMapper,
                "data/suppliers.json",
                UrlConstants.SUPPLIER_POST_ENDPOINT,
                repository);
    }

    @Override
    protected TypeReference<List<Supplier>> getTypeReference() {
        return new TypeReference<>() {
        };
    }

    @Override
    protected SupplierMessage convertToMessage(Supplier supplier) {
        SupplierMessage supplierMessage = new SupplierMessage();
        supplierMessage.setFirst_name(supplier.getFirstName());
        supplierMessage.setLast_name(supplier.getLastName());
        supplierMessage.setPhone_number(supplier.getPhoneNumber());
        supplierMessage.setEmail(supplier.getEmail());
        supplierMessage.setLatitude(supplier.getLatitude());
        supplierMessage.setLongitude(supplier.getLongitude());
        return supplierMessage;
    }

    @Override
    protected String getEntityIdentifier(Supplier entity) {
        return String.valueOf(entity.getSupplier_id());
    }
}
