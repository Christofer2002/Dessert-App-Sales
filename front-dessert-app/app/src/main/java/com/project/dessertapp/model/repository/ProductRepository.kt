package com.project.dessertapp.model.repository

import android.util.Log
import com.project.dessertapp.model.entities.Product
import com.project.dessertapp.model.network.product.ProductService
import retrofit2.Response
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productService: ProductService
) {
    /**
     * Fetches all products from the API and returns them as a list.
     *
     * @return A list of Product objects, or an empty list if an error occurs.
     */
    suspend fun getAllProducts(token: String?): List<Product> {
        return if (token != null) {
            safeApiCallList {
                productService.getAllProducts(token)
            } ?: emptyList() // Return a list of products if the token is not null
        } else {
            emptyList() // Return an empty list if the token is null
        }
    }


    /**
     * Fetches a product by its ID from the API.
     *
     * @param id The ID of the product to fetch.
     * @return The Product object or null if an error occurs.
     */
    suspend fun getProductById(id: Long, token: String?): Product? {
        return safeApiCall { productService.getProductById(token, id) }
    }

    suspend fun getProductsByCategoryId(categoryId: Long, token: String?): List<Product> {
        return safeApiCallList { productService.getProductsByCategoryId(token, categoryId) }
    }

    suspend fun getProductsByEventId(eventId: Long?, token: String?): List<Product> {
        return safeApiCallList { productService.getProductsByEventId(token, eventId) }
    }

    /**
     * Adds a new product to the API and returns the added product.
     *
     * @param product The Product object to add.
     * @return The added Product object or null if an error occurs.
     */
    suspend fun addProduct(product: Product, token: String?): Product? {
        return safeApiCall { productService.addProduct(token, product) }
    }

    /**
     * Deletes a product by its ID from the API.
     *
     * @param id The ID of the product to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    suspend fun deleteProductById(id: Long, token: String?): Boolean {
        return try {
            val response = productService.deleteProduct(token, id)
            response.isSuccessful // Return true if the deletion was successful
        } catch (e: Exception) {
            false // Return false if any error occurs
        }
    }

    /**
     * A generic function to safely handle API calls that return lists.
     * @param apiCall A suspend lambda that returns a Response<List<T>>.
     * @return A list of results if successful or an empty list if an error occurs.
     */
    private suspend fun <T> safeApiCallList(apiCall: suspend () -> Response<List<T>>): List<T> {
        return try {
            val response = apiCall.invoke()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("Repository", "API call failed: ${e.message}")
            emptyList()
        }
    }

    /**
     * Updates a product by its ID in the API and returns the updated product.
     *
     * @param productId The ID of the product to update.
     * @return The updated Product object or null if an error occurs.
     */
    suspend fun updateProduct(
        token: String?,
        productId: Long,
        name: String? = null,
        price: Double? = null,
        description: String? = null,
        image: String? = null,
        flavour: String? = null,
        durationDays: Int? = null,
        categoryId: Long? = null
    ): Product? {
        // Create a new Product object with the updated information
        val updatedProduct = Product(
            id = productId,
            name = name ?: "",  // Usa el valor proporcionado o una cadena vacía si es null
            unitPrice = price ?: 0.0,
            description = description ?: "",
            image = image ?: "",
            flavour = flavour ?: "",
            durationDays = durationDays ?: 0,
            categoryId = categoryId ?: 0L
        )

        // Call the API to update the product
        return try {
            val response = productService.updateProduct(token, productId, updatedProduct)
            if (response.isSuccessful) {
                response.body()  // Return the updated product if successful
            } else {
                null  // If the response is not successful, return null
            }
        } catch (e: Exception) {
            null  // If an exception occurs, return null
        }
    }

    /**
     * A generic function to safely handle API calls that return a single object.
     * @param apiCall A suspend lambda that returns a Response<T>.
     * @return The result object if successful or null if an error occurs.
     */
    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): T? {
        return try {
            val response = apiCall.invoke()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
