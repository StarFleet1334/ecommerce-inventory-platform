package com.example.orderprocessingservice.validator.base;

public class ValidatorContext {
    private static final ThreadLocal<Object> currentEntity = new ThreadLocal<>();

    @SuppressWarnings("unchecked")
    public static <T> T getCurrentEntity() {
        return (T) currentEntity.get();
    }

    public static void setCurrentEntity(Object entity) {
        currentEntity.set(entity);
    }

    public static void clear() {
        currentEntity.remove();
    }
}
