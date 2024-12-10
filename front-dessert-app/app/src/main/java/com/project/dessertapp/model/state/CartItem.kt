package com.project.dessertapp.model.state

import com.project.dessertapp.model.entities.Product

data class CartItem(
    val product: Product,
    var quantity: Int
)
