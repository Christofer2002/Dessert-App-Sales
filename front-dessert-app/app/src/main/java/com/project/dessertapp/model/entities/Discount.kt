package com.project.dessertapp.model.entities

import java.util.Date

data class Discount(
    val id: Long,
    val code: String,
    val description: String,
    val discountPercentage: Double,
    val validFrom: Date,
    val validTo: Date
)