package com.project.dessertapp.model.datasources

import com.project.dessertapp.model.entities.Category

object CategoryProvider {

    private val categoriesList = listOf(
        Category(1, "Cakes", "https://source.unsplash.com/featured/?cake"),
        Category(2, "Cupcakes", "https://source.unsplash.com/featured/?cupcake"),
        Category(3, "Donuts", "https://source.unsplash.com/featured/?donut"),
        Category(4, "Brownies", "https://source.unsplash.com/featured/?brownie"),
        Category(5, "Cereal", "https://source.unsplash.com/featured/?cereal"),
    )

    fun findAllCategories(): List<Category> {
        return categoriesList
    }
}