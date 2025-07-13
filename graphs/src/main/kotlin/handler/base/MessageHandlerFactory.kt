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
        logger.debug("Available handlers: ${
            handlers.entries.joinToString("\n") {
                "${it.key} -> ${it.value.javaClass.simpleName}"
            }
        }")
        val handler = handlers[topic]
        if (handler == null) {
            logger.error("No handler found for topic: $topic")
            throw IllegalArgumentException("No handler found for topic: $topic")
        }
        logger.info("Found handler ${handler.javaClass.simpleName} for topic: $topic")
        return handler
    }

}
