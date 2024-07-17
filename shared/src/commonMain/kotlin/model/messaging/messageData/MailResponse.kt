package model.messaging.messageData

import kotlinx.serialization.Serializable

@Serializable
sealed class MailResponse {
    @Serializable
    data class Success(
        val messageSent: Boolean
    ) : MailResponse()

    @Serializable
    data class Error(
        val exceptionMessage: String,
        val stackTrace: String
    ) : MailResponse() {
        companion object {
            fun withException(ex: Exception): Error {
                return Error(ex.message ?: "UnKnown Error.", ex.stackTraceToString())
            }
        }
    }
}