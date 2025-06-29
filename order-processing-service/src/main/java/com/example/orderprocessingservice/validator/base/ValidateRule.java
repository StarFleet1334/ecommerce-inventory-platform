package com.example.orderprocessingservice.validator.base;

import org.apache.commons.lang3.tuple.Pair;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class ValidateRule<T> {
    private final String fieldName;
    private final Function<T, Object> getter;
    private final List<Pair<Predicate<Object>, String>> validations;

    private ValidateRule(String fieldName, Function<T, Object> getter) {
        this.fieldName = fieldName;
        this.getter = getter;
        this.validations = new ArrayList<>();
    }

    public static <T> ValidateRule<T> forField(String fieldName, Function<T, Object> getter) {
        return new ValidateRule<>(fieldName, getter);
    }

    public ValidateRule<T> required() {
        validations.add(Pair.of(
                Objects::nonNull,
                fieldName + " is required"
        ));
        return this;
    }

    public ValidateRule<T> maxLength(int maxLength) {
        validations.add(Pair.of(
                obj -> obj == null || obj.toString().length() <= maxLength,
                fieldName + " must not exceed " + maxLength + " characters"
        ));
        return this;
    }

    public ValidateRule<T> unique(Predicate<Object> uniqueCheck) {
        validations.add(Pair.of(
                obj -> obj == null || !uniqueCheck.test(obj),
                fieldName + " already exists"
        ));
        return this;
    }

    public List<String> validate(T entity) {
        List<String> errors = new ArrayList<>();
        Object value = getter.apply(entity);

        for (var validation : validations) {
            if (!validation.getLeft().test(value)) {
                errors.add(validation.getRight());
            }
        }
        return errors;
    }

    public ValidateRule<T> latitude() {
        validations.add(Pair.of(
                obj -> obj == null ||
                        ((BigDecimal) obj).compareTo(new BigDecimal("-90.0")) >= 0 &&
                                ((BigDecimal) obj).compareTo(new BigDecimal("90.0")) <= 0,
                "Latitude must be between -90 and 90"
        ));
        return this;
    }

    public ValidateRule<T> longitude() {
        validations.add(Pair.of(
                obj -> obj == null ||
                        ((BigDecimal) obj).compareTo(new BigDecimal("-180.0")) >= 0 &&
                                ((BigDecimal) obj).compareTo(new BigDecimal("180.0")) <= 0,
                "Longitude must be between -180 and 180"
        ));
        return this;
    }

    public ValidateRule<T> minValue(int minValue) {
        validations.add(Pair.of(
                obj -> obj == null || ((Number) obj).intValue() >= minValue,
                fieldName + " must be greater than or equal to " + minValue
        ));
        return this;
    }

    public ValidateRule<T> dependentField(String otherFieldName,
                                          Function<T, Object> otherGetter,
                                          BiPredicate<Object, Object> condition,
                                          String errorMessage) {
        validations.add(Pair.of(
                value -> {
                    T entity = ValidatorContext.getCurrentEntity();
                    Object otherValue = otherGetter.apply(entity);
                    return value == null || otherValue == null || condition.test(value, otherValue);
                },
                errorMessage
        ));
        return this;
    }


}
