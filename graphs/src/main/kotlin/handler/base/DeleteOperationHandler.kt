package org.example.handler.base

import org.springframework.stereotype.Component

@Component
abstract class DeleteOperationHandler : MessageHandler {
    override fun handle(message: String) {
        processDelete(message)
    }

    protected abstract fun processDelete(message: String)
}