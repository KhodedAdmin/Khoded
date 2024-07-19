package com.probro.khoded.configurations

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable

fun Application.configureSessions() {
    install(Sessions) {
        cookie<UserCookie>("UserCookie")
    }
}

@Serializable
data class UserCookie(
    val userID: String,
    val userToken: String,
    val timestamp: String
)