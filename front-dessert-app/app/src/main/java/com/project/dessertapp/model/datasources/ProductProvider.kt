package com.project.dessertapp.model.datasources

import com.project.dessertapp.model.entities.Product

/**
 * This is a temporary class to simulate the interaction with data for Products and Categories
 */
object ProductProvider {

    private val productsList = mutableListOf(
        Product(
            id = 1,
            name = "Cookie Monster Cake",
            flavour = "Chocolate",
            durationDays = 3,
            unitPrice = 15000.0,
            description = "A delicious cake with a cookie monster design",
            image = "img",
            categoryId = 1
        ),
        Product(
            id = 2,
            name = "Birthday Cake",
            flavour = "Vanilla",
            durationDays = 5,
            unitPrice = 10000.0,
            description = "A classic birthday cake with a candle on top",
            image = "img",
            categoryId = 1
        ),
        Product(
            id = 3,
            name = "Champions Cake",
            flavour = "Strawberry",
            durationDays = 4,
            unitPrice = 12000.0,
            description = "A cake for the champions",
            image = "img",
            categoryId = 1
        ),
        Product(
            id = 4,
            name = "Smile Cake",
            flavour = "Lemon",
            durationDays = 2,
            unitPrice = 8500.0,
            description = "A cake that will make you smile",
            image = "img",
            categoryId = 1
        ),
        Product(
            id = 5,
            name = "Vanilla Cupcake",
            flavour = "Vanilla",
            durationDays = 1,
            unitPrice = 3000.0,
            image = "img",
            description = "A classic vanilla cupcake",
            categoryId = 2
        ),
        Product(
            id = 6,
            name = "Chocolate Cupcake",
            flavour = "Chocolate",
            durationDays = 1,
            unitPrice = 3500.0,
            image = "img",
            description = "A delicious chocolate cupcake",
            categoryId = 2
        ),
        Product(
            id = 7,
            name = "Classic Donut",
            flavour = "Glazed",
            durationDays = 1,
            unitPrice = 2500.0,
            description = "A classic glazed donut",
            image = "img",
            categoryId = 3
        ),
        Product(
            id = 8,
            name = "Sprinkle Donut",
            flavour = "Strawberry",
            durationDays = 1,
            unitPrice = 2700.0,
            description = "A donut with sprinkles on top",
            image = "img",
            categoryId = 3
        ),
        Product(
            id = 9,
            name = "Fudge Brownie",
            flavour = "Chocolate",
            durationDays = 2,
            unitPrice = 4000.0,
            description = "A delicious fudge brownie",
            image = "img",
            categoryId = 4
        ),
        Product(
            id = 10,
            name = "FME Cake",
            flavour = "Chocolate",
            durationDays = 2,
            unitPrice = 54000.0,
            description = "A delicious cake with a cookie monster design",
            image = "img",
            categoryId = 1
        ),
        Product(
            id = 11,
            name = "Chris Cake",
            flavour = "Chocolate",
            durationDays = 2,
            unitPrice = 54000.0,
            description = "A delicious cake with a cookie monster design",
            image = "img",
            categoryId = 1
        ),
        Product(
            id = 12,
            name = "Alejo Cake",
            flavour = "Chocolate",
            durationDays = 2,
            unitPrice = 54000.0,
            description = "A delicious cake with a cookie monster design",
            image = "img",
            categoryId = 1
        )
    )

    fun findAllProduct(): List<Product> {
        return productsList
    }

    fun addProduct(product: Product) {
        productsList.add(product) // Agrega el producto a la lista
    }

    /**
     * Update an existing product in the list.
     */
    fun updateProduct(product: Product) {
        val index = productsList.indexOfFirst { it.id == product.id }
        if (index != -1) {
            productsList[index] = product // Modifica el producto en la lista
        }
    }

    fun deleteProduct(productId: Long) {
        productsList.removeAll { it.id == productId } // Elimina el producto de la lista
    }
}
