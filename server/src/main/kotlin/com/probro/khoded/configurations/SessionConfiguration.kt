package com.probro.khoded.configurations

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable

fun Application.configureSessions() {
    install(Sessions) {
        cookie<UserCookie>(Cookies.USER_COOKIE.value) {
            cookie.maxAgeInSeconds = 15
//            cookie.secure = true TODO : UN -COMMENT BEFORE MOVING TO PRODUCTION.
        }
    }
}

@Serializable
data class UserCookie(
    val userID: String,
    val userToken: String,
    val role: String,
    val timestamp: String
)

enum class Cookies(val value: String) {
    USER_COOKIE("user-cookie")
}