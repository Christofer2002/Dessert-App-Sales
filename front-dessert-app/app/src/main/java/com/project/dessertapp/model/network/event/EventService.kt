package com.project.dessertapp.model.network.event

import com.project.dessertapp.model.entities.Category
import com.project.dessertapp.model.entities.Event
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface EventService {
    /**
     * Fetches all events from the API.
     *
     * @return A Response object containing a list of Event objects.
     */
    @GET("events")
    suspend fun getAllEvents(
        @Header("Authorization") token: String?  // Authorization header with token
    ): Response<List<Event>>

    /**
     * Fetches a event by its ID from the API.
     *
     * @param id The ID of the event to fetch.
     * @return A Response object containing the Event object.
     */
    @GET("events/ev")
    suspend fun getEventById(@Query("id") id: Long): Response<Event>
}