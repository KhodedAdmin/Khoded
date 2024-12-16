package com.probro.khoded.model.local.dto

import kotlinx.serialization.Serializable

@Serializable
data class ServiceDTO(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var duration: String = "",
    var price: Price = Price(),
    var graphic: String = "",
)

@Serializable
data class Price(
    val amount: Double = 0.0,
    val currency: String = "0"
)