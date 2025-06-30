package com.example.orderprocessingservice.mapper.base;

public interface BaseMapper<D,E> {
    E toEntity(D dto);
}
