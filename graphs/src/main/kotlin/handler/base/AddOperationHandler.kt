package org.example.handler.base

import org.springframework.stereotype.Component

@Component
abstract class AddOperationHandler : MessageHandler {
    override fun handle(message: String) {
        processAdd(message)
    }

    protected abstract fun processAdd(message: String)
}