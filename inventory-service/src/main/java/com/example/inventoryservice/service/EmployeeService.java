package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.messages.EmployeeMessage;
import com.example.inventoryservice.event.publisher.EmployeeEventPB;
import com.example.inventoryservice.event.remover.EmployeeEventRM;
import com.example.inventoryservice.utils.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeEventPB employeeEventPB;
    private final EmployeeEventRM employeeEventRM;

    @Value("${rocketmq.employee_add_topic}")
    private String ADD_EMPLOYEE_TOPIC;

    @Value("${rocketmq.employee_delete_topic}")
    private String DELETE_EMPLOYEE_TOPIC;

    public void sendEmployeeCreateMessage(EmployeeMessage employee) {
        employeeEventPB.sentMessage(ADD_EMPLOYEE_TOPIC, EventType.CREATED.getMessage(), employee);
    }

    public void sendEmployeeDeleteMessage(String employeeId) {
        employeeEventRM.sentMessage(DELETE_EMPLOYEE_TOPIC, EventType.DELETED.getMessage(), employeeId);
    }
}
