package com.example.orderprocessingservice.consumer;

import com.example.orderprocessingservice.handler.MessageHandler;
import com.example.orderprocessingservice.handler.MessageHandlerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderMessageConsumer.class);
    private final DefaultMQPushConsumer consumer;
    private final ObjectMapper objectMapper;
    private final MessageHandlerFactory messageHandlerFactory;

    /****
     *
     * Customer topic
     *
     */
    @Value("${rocketmq.customer_add_topic}")
    private String customerAddTopic;

    @Value("${rocketmq.customer_delete_topic}")
    private String customerDeleteTopic;

    /****
     *
     * Employee topic
     *
     */
    @Value("${rocketmq.employee_add_topic}")
    private String employeeAddTopic;

    @Value("${rocketmq.employee_delete_topic}")
    private String employeeDeleteTopic;

    /****
     *
     * Product topic
     *
     */
    @Value("${rocketmq.product_add_topic}")
    private String productAddTopic;

    @Value("${rocketmq.product_delete_topic}")
    private String productDeleteTopic;

    /****
     *
     * Stock topic
     *
     */

    @Value("${rocketmq.stock_add_topic}")
    private String stockAddTopic;

    @Value("${rocketmq.stock_delete_topic}")
    private String stockDeleteTopic;

    /****
     *
     * Supplier topic
     *
     */

    @Value("${rocketmq.supplier_add_topic}")
    private String supplierAddTopic;

    @Value("${rocketmq.supplier_delete_topic}")
    private String supplierDeleteTopic;


    /****
     *
     * WareHouse topic
     *
     */

    @Value("${rocketmq.warehouse_add_topic}")
    private String warehouseAddTopic;

    @Value("${rocketmq.warehouse_delete_topic}")
    private String warehouseDeleteTopic;


    @PostConstruct
    public void init() throws Exception {
        consumer.subscribe(customerAddTopic, "*");
        consumer.subscribe(customerDeleteTopic, "*");

        consumer.subscribe(employeeAddTopic, "*");
        consumer.subscribe(employeeDeleteTopic, "*");

        consumer.subscribe(productAddTopic, "*");
        consumer.subscribe(productDeleteTopic, "*");

        consumer.subscribe(stockAddTopic, "*");
        consumer.subscribe(stockDeleteTopic, "*");

        consumer.subscribe(supplierAddTopic, "*");
        consumer.subscribe(supplierDeleteTopic, "*");

        consumer.subscribe(warehouseAddTopic, "*");
        consumer.subscribe(warehouseDeleteTopic, "*");

        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                String topic = msg.getTopic();
                LOGGER.info("Received message from topic: {}", topic);
                MessageHandler handler = messageHandlerFactory.getHandler(topic);

                if (handler != null) {
                    return handler.handle(msg, objectMapper);
                } else {
                    LOGGER.error("No handler found for topic: {}", topic);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();
    }

    @PreDestroy
    public void destroy() {
        Optional.ofNullable(consumer).ifPresent(DefaultMQPushConsumer::shutdown);
    }
}