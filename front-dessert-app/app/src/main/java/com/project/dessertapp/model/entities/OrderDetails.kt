package com.project.dessertapp.model.entities

class OrderDetails (
    val id: Long? = null,
    val amount: Int,
    val order: Order? = null,
    val product: Product
)