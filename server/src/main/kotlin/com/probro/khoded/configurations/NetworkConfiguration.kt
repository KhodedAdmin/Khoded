package com.probro.khoded.configurations

import com.probro.khoded.model.local.dto.LoginDTO
import com.probro.khoded.model.local.dto.UserDTO
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureNetwork() {
    install(CORS)
    install(AutoHeadResponse)
    install(RequestValidation) {
        validate<LoginDTO> { user ->
            when {
                user.password != user.confirmPassword -> {
                    ValidationResult.Invalid("Passwords do not match.")
                }

                user.email?.isNullOrEmpty() == true && user.username?.isNullOrEmpty() == true -> {
                    ValidationResult.Invalid("Please provide either a username or email for the account.")
                }

                else -> {
                    ValidationResult.Valid
                }
            }

        }
    }
}