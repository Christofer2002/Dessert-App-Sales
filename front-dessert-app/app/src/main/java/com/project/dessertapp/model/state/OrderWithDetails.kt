package com.project.dessertapp.model.state

import com.project.dessertapp.model.entities.Order
import com.project.dessertapp.model.entities.OrderDetails

data class OrderWithDetails(
    val order: Order,
    val orderDetails: List<OrderDetails>
)
