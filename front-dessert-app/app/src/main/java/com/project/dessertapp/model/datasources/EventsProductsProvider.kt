package com.project.dessertapp.model.datasources
import com.project.dessertapp.model.entities.EventsProducts

/**
 * This is a temporary class to simulate the interaction with data for EventProduct relations
 */
object EventsProductsProvider {

    // List of event-product relationships
    private val eventProductsList = mutableListOf(
        EventsProducts(productId = 1, eventId = 1),
        EventsProducts(productId = 2, eventId = 1),
        EventsProducts(productId = 3, eventId = 2),
        EventsProducts(productId = 4, eventId = 3),
        EventsProducts(productId = 5, eventId = 4)
    )

    // Function to retrieve all event-product relationships
    fun findAllEventProducts(): List<EventsProducts> {
        return eventProductsList
    }

    // Function to retrieve products by event ID
    fun findProductsByEventId(eventId: Long): List<EventsProducts> {
        return eventProductsList.filter { it.eventId == eventId }
    }

    // Function to retrieve events by product ID
    fun findEventsByProductId(productId: Long): List<EventsProducts> {
        return eventProductsList.filter { it.productId == productId }
    }

    // Function to add a new event-product relationship
    fun addEventProduct(eventProduct: EventsProducts) {
        eventProductsList.add(eventProduct)
    }

    // Function to remove a specific event-product relationship
    fun deleteEventProduct(productId: Long, eventId: Long) {
        eventProductsList.removeAll { it.productId == productId && it.eventId == eventId }
    }
}
