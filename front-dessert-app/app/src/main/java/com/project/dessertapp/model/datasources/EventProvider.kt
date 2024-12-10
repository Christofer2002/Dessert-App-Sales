package com.project.dessertapp.model.datasources

import com.project.dessertapp.model.entities.Event

/**
 * This is a temporary class to simulate the interaction with data for Events
 */
object EventProvider {

    // List of events
    private val eventsList = listOf(
        Event(
            id = 1,
            name = "Birthday Special",
            description = "Celebrate your birthday with our special cakes",
            image = "birthday_special.png"
        ),
        Event(
            id = 2,
            name = "Valentine's Day Offer",
            description = "Sweeten your Valentine's Day with our cupcakes",
            image = "valentines_day_offer.png"
        ),
        Event(
            id = 3,
            name = "Christmas Sale",
            description = "Enjoy the holiday season with our Christmas-themed desserts",
            image = "christmas_sale.png"
        ),
        Event(
            id = 4,
            name = "Summer Special",
            description = "Beat the heat with our refreshing summer treats",
            image = "summer_special.png"
        )
    )

    // Function to retrieve all events
    fun findAllEvents(): List<Event> {
        return eventsList
    }

    // Function to retrieve a specific event by its ID
    fun getEventById(eventId: Long): Event? {
        return eventsList.find { it.id == eventId }
    }
}
