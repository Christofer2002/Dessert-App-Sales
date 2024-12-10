package com.project.dessertapp.model.entities

import com.project.dessertapp.model.state.StatusDTO
import java.util.Date

data class Order(
    val id: Long? = null,
    val orderDate: Date,
    val deliverDate: Date,
    val totalPrice: Double,
    val deliveryInstructions: String,
    val user: User? = null,
    var status: StatusDTO,
    val discount: Discount? = null
)