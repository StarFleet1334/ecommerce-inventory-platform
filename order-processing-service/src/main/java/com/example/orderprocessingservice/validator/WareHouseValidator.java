package com.example.orderprocessingservice.validator;

import com.example.orderprocessingservice.repository.personnel.WareHouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WareHouseValidator {
    private final WareHouseRepository wareHouseRepository;
}
