package com.probro.khoded.model.remote.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoogleUserInfo(
    val email: String,
    @SerialName("family_name")
    val familyName: String,
    @SerialName("given_name")
    val givenName: String,
    val id: String,
    val name: String,
    val picture: String,
    @SerialName("verified_email")
    val verifiedEmail: Boolean
)