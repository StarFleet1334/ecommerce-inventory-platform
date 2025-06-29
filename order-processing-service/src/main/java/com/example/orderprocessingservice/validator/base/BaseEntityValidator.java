package com.example.orderprocessingservice.validator.base;

import jakarta.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseEntityValidator<T> {
    protected final List<ValidateRule<T>> rules = new ArrayList<>();

    public void validate(T entity) {
        List<String> errors = new ArrayList<>();
        try {
            ValidatorContext.setCurrentEntity(entity);
            for (ValidateRule<T> rule : rules) {
                errors.addAll(rule.validate(entity));
            }
        } finally {
            ValidatorContext.clear();
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(String.join(", ", errors));
        }
    }


}
