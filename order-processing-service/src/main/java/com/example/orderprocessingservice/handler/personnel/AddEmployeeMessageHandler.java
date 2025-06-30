package com.example.orderprocessingservice.handler.personnel;

import com.example.orderprocessingservice.dto.eventDto.EmployeeMP;
import com.example.orderprocessingservice.handler.MessageHandler;
import com.example.orderprocessingservice.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddEmployeeMessageHandler implements MessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddEmployeeMessageHandler.class);
    private final EmployeeService employeeService;

    @Override
    public ConsumeConcurrentlyStatus handle(MessageExt message, ObjectMapper objectMapper) {
        try {
            String messageBody = new String(message.getBody());
            LOGGER.info("Processing ADD employee message: {}", messageBody);

            EmployeeMP employee_message = objectMapper.readValue(messageBody, EmployeeMP.class);
            employeeService.handleNewEmployee(employee_message);

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            LOGGER.error("Error processing ADD employee message: {}", e.getMessage(), e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}
