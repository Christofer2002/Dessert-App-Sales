package com.project.dessertapp.model.network.product

import com.project.dessertapp.model.entities.Product
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {

    /**
     * Fetches all products from the API.
     *
     * @param token The authorization token.
     * @return A Response object containing a list of product objects.
     */
    @GET("products")
    suspend fun getAllProducts(
        @Header("Authorization") token: String?
    ): Response<List<Product>>

    /**
     * Fetches a product by its ID from the API.
     *
     * @param token The authorization token.
     * @param id The ID of the product to fetch.
     * @return A Response object containing the Product object.
     */
    @GET("products/pd")
    suspend fun getProductById(
        @Header("Authorization") token: String?,
        @Query("id") id: Long
    ): Response<Product>

    /**
     * Fetches products by category ID from the API.
     *
     * @param token The authorization token.
     * @param categoryId The ID of the category.
     * @return A Response object containing a list of Product objects.
     */
    @GET("products/category")
    suspend fun getProductsByCategoryId(
        @Header("Authorization") token: String?,
        @Query("id") categoryId: Long
    ): Response<List<Product>>

    /**
     * Fetches products by event ID from the API.
     *
     * @param token The authorization token.
     * @param eventId The ID of the event.
     * @return A Response object containing a list of Product objects.
     */
    @GET("event-product/event")
    suspend fun getProductsByEventId(
        @Header("Authorization") token: String?,
        @Query("id") eventId: Long?
    ): Response<List<Product>>

    /**
     * Adds a new product to the API.
     *
     * @param token The authorization token.
     * @param product The Product object to add.
     * @return A Response object containing the newly added Product object.
     */
    @POST("products")
    suspend fun addProduct(
        @Header("Authorization") token: String?,
        @Body product: Product
    ): Response<Product>

    /**
     * Deletes a product by its ID from the API.
     *
     * @param token The authorization token.
     * @param id The ID of the product to delete.
     * @return A Response object confirming the deletion.
     */
    @DELETE("products")
    suspend fun deleteProduct(
        @Header("Authorization") token: String?,
        @Query("id") id: Long
    ): Response<Unit>

    /**
     * Updates a product by its ID in the API.
     *
     * @param token The authorization token.
     * @param id The ID of the product to update.
     * @param product The Product object containing updated information.
     * @return A Response object containing the updated Product.
     */
    @PUT("products")
    suspend fun updateProduct(
        @Header("Authorization") token: String?,
        @Query("id") id: Long,
        @Body product: Product
    ): Response<Product>
}