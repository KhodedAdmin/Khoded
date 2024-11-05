package com.probro.khoded.model.local.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    var id: String? = "",
    var name: String? = "",
    var email: String? = "",
    var phone: String? = "",
    var password: String? = "",
    var confirmPassword: String? = "",
    var role: String? = Role.GUEST.value
)

enum class Role(var value: String) {
    GUEST("guest"), CUSTOMER("customer"), EMPLOYEE("employee"), ADMIN("admin")
}