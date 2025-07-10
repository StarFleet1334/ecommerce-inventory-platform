package com.example.orderprocessingservice.config.database.loaders;

import com.example.orderprocessingservice.config.database.AbstractDataLoader;
import com.example.orderprocessingservice.dto.messages.CustomerMessage;
import com.example.orderprocessingservice.dto.model.customer.Customer;
import com.example.orderprocessingservice.repository.customer.CustomerRepository;
import com.example.orderprocessingservice.utils.constants.UrlConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class CustomerLoader extends AbstractDataLoader<Customer, CustomerMessage> {

    @Autowired
    public CustomerLoader(ObjectMapper objectMapper,
                          CustomerRepository repository) {
        super(objectMapper,
                "data/customers.json",
                UrlConstants.CUSTOMER_POST_ENDPOINT,
                repository);
    }

    @Override
    protected TypeReference<List<Customer>> getTypeReference() {
        return new TypeReference<>() {
        };
    }

    @Override
    protected CustomerMessage convertToMessage(Customer customer) {
        CustomerMessage customerMessage = new CustomerMessage();
        customerMessage.setFirst_name(customer.getFirstName());
        customerMessage.setLast_name(customer.getLastName());
        customerMessage.setPhone_number(customer.getPhoneNumber());
        customerMessage.setEmail(customer.getEmail());
        customerMessage.setLatitude(customer.getLatitude());
        customerMessage.setLongitude(customer.getLongitude());
        return customerMessage;
    }

    @Override
    protected String getEntityIdentifier(Customer entity) {
        return entity.getFirstName() + " " + entity.getLastName();
    }
}
