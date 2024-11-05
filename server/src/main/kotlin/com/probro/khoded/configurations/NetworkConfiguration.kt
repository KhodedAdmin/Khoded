package com.probro.khoded.configurations

import com.probro.khoded.model.local.dto.UserDTO
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureNetwork() {
    install(CORS)
    install(AutoHeadResponse)
    install(RequestValidation) {
        validate<UserDTO> { user ->
            when {
                user.password != user.confirmPassword -> {
                    ValidationResult.Invalid("Passwords do not match.")
                }

                user.name?.isNullOrEmpty() == true -> {
                    ValidationResult.Invalid("Name is a required field.")
                }

                user.email?.isNullOrEmpty() == true && user.phone?.isNullOrEmpty() == true -> {
                    ValidationResult.Invalid("Contact info is required for account validation.")
                }

                else -> {
                    ValidationResult.Valid
                }
            }

        }
    }
}