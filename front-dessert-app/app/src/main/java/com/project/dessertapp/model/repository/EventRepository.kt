package com.project.dessertapp.model.repository

import com.project.dessertapp.model.entities.Event
import com.project.dessertapp.model.network.event.EventService
import retrofit2.Response
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val eventService: EventService
) {
    /**
     * Fetches all events from the API and returns them as a list.
     *
     * @return A list of Events objects, or an empty list if an error occurs.
     */
    suspend fun getAllEvents(token : String?): List<Event> {
        return safeApiCallList { eventService.getAllEvents(token) }
    }

    /**
     * Fetches a product by its ID from the API.
     *
     * @param id The ID of the product to fetch.
     * @return The Product object or null if an error occurs.
     */
    suspend fun getEventById(id: Long): Event? {
        return safeApiCall { eventService.getEventById(id) }
    }

    /**
     * Adds a new product to the API and returns the added product.
     *
     * @param product The Product object to add.
     * @return The added Product object or null if an error occurs.
     *//*
    suspend fun addProduct(product: Product): Product? {
        return safeApiCall { productService.addProduct(product) }
    }

    *//**
     * Deletes a product by its ID from the API.
     *
     * @param id The ID of the product to delete.
     * @return True if the deletion was successful, false otherwise.
     *//*
    suspend fun deleteProductById(id: Long): Boolean {
        return try {
            val response = productService.deleteProduct(id)
            response.isSuccessful // Return true if the deletion was successful
        } catch (e: Exception) {
            false // Return false if any error occurs
        }
    }*/

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
            emptyList()
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