package com.project.dessertapp.model.entities

data class Product(
    val id: Long? = null,
    val name: String,
    val flavour: String,
    val durationDays: Int,
    val unitPrice: Double,
    val description: String,
    val image: String,
    val categoryId: Long,
)