package com.project.dessertapp.model.network.category

import com.project.dessertapp.model.entities.Category
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryService {
    /**
     * Fetches all tasks from the API.
     *
     * @return A Response object containing a list of Task objects.
     */
    @GET("categories")
    suspend fun getAllCategories(
        @Header("Authorization") token: String?  // Authorization header with token
    ): Response<List<Category>>

    /**
     * Fetches a task by its ID from the API.
     *
     * @param id The ID of the task to fetch.
     * @return A Response object containing the Task object.
     */
    @GET("categories/ct")
    suspend fun getCategoryById(@Query("id") id: Long): Response<Category>
}