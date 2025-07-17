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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MessagesConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesConsumer.class);
    private final DefaultMQPushConsumer consumer;
    private final ObjectMapper objectMapper;
    private final MessageHandlerFactory messageHandlerFactory;

    /****
     * <p>
     * Customer topic
     *
     */
    @Value("${rocketmq.customer_add_topic}")
    private String customerAddTopic;

    @Value("${rocketmq.customer_delete_topic}")
    private String customerDeleteTopic;

    /****
     * <p>
     * Employee topic
     *
     */
    @Value("${rocketmq.employee_add_topic}")
    private String employeeAddTopic;

    @Value("${rocketmq.employee_delete_topic}")
    private String employeeDeleteTopic;

    /****
     * <p>
     * Product topic
     *
     */
    @Value("${rocketmq.product_add_topic}")
    private String productAddTopic;

    @Value("${rocketmq.product_delete_topic}")
    private String productDeleteTopic;

    /****
     * <p>
     * Stock topic
     *
     */

    @Value("${rocketmq.stock_add_topic}")
    private String stockAddTopic;

    @Value("${rocketmq.stock_delete_topic}")
    private String stockDeleteTopic;

    /****
     * <p>
     * Supplier topic
     *
     */

    @Value("${rocketmq.supplier_add_topic}")
    private String supplierAddTopic;

    @Value("${rocketmq.supplier_delete_topic}")
    private String supplierDeleteTopic;


    /****
     * <p>
     * WareHouse topic
     *
     */

    @Value("${rocketmq.warehouse_add_topic}")
    private String warehouseAddTopic;

    @Value("${rocketmq.warehouse_delete_topic}")
    private String warehouseDeleteTopic;

    /****
     * <p>
     *
     * Customer Order
     *
     */

    @Value("${rocketmq.customer_order_topic}")
    private String customerOrderTopic;

    /****
     * <p>
     *
     * Supply
     *
     *
     */

    @Value("${rocketmq.supply_add_topic}")
    private String supplyAddTopic;

    @Value("${rocketmq.supply_delete_topic}")
    private String supplyDeleteTopic;

    @PostConstruct
    public void init() throws Exception {
        for (String topic : getAllTopics()) {
            consumer.subscribe(topic, "CREATED || UPDATED || DELETED");
            LOGGER.info("Subscribed to topic: {}", topic);
        }

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

    private List<String> getAllTopics() {
        return Arrays.asList(
                customerAddTopic,
                customerDeleteTopic,
                employeeAddTopic,
                employeeDeleteTopic,
                productAddTopic,
                productDeleteTopic,
                stockAddTopic,
                stockDeleteTopic,
                supplierAddTopic,
                supplierDeleteTopic,
                warehouseAddTopic,
                warehouseDeleteTopic,
                customerOrderTopic,
                supplyAddTopic,
                supplyDeleteTopic
        );
    }

}