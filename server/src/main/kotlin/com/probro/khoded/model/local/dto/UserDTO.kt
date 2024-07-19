package com.probro.khoded.model.local.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val role: Role = Role.GUEST
)

enum class Role {
    GUEST, CUSTOMER, EMPLOYEE, ADMIN
}