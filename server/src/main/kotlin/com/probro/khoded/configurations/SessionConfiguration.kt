package com.probro.khoded.configurations

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
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
    var userID: String? = "",
    val userToken: String? = "",
    val oAuthState: String? = "",
    var role: String? = "",
    val timestamp: String
)



enum class Cookies(val value: String) {
    USER_COOKIE("user-cookie")
}