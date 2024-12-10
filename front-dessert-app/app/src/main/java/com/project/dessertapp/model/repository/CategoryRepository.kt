package com.project.dessertapp.model.repository

import com.project.dessertapp.model.entities.Category
import com.project.dessertapp.model.network.category.CategoryService
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryService: CategoryService
) {
    /**
     * Fetches all products from the API and returns them as a list.
     *
     * @return A list of Categories objects or null if an error occurs.
     */

    suspend fun getAllCategories(token : String?): List<Category>? {
        return try {
            val response = categoryService.getAllCategories(token)
            if (response.isSuccessful) {
                // If response is successful but the body is null, handle that case
                response.body() ?: emptyList()
            } else {
                // Log or handle error response case
                null
            }
        } catch (e: Exception) {
            // Catching any exceptions that may occur during the network call
            e.printStackTrace()
            null
        }
    }

    /**
     * Fetches a category by its ID from the API.
     *
     * @param id The ID of the category to fetch.
     * @return The Category object or null if an error occurs.
     */
    suspend fun getCategoryById(id: Long): Category? {
        return try {
            val response = categoryService.getCategoryById(id)
            if (response.isSuccessful) {
                response.body() // Handle null body here
            } else {
                // Log or handle error response case
                null
            }
        } catch (e: Exception) {
            // Catching any exceptions that may occur during the network call
            e.printStackTrace()
            null
        }
    }
}