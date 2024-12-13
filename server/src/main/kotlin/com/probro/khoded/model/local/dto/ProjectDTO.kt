package com.probro.khoded.model.local.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectDTO(
    val name: String = "",
    val description: String = "",
    val customer: String = "",
    @SerialName("consultation_id")
    val consultationID: String = ""
)
