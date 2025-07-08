package org.example.handler.base

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class MessageHandlerFactory(
    @Qualifier("messageHandlers")
    private val handlers: Map<String, MessageHandler>
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("MessageHandlerFactory initialized with handlers for topics: ${handlers.keys}")
    }

    fun getHandler(topic: String): MessageHandler {
        logger.info("Looking for handler for topic: $topic")
        logger.info("Available handlers: {}, topics: {}", handlers.keys,handlers.values)
        return handlers[topic] ?: throw IllegalArgumentException("No handler found for topic: $topic")
    }
}
