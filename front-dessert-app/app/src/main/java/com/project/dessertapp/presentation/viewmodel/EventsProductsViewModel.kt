package com.project.dessertapp.presentation.viewmodel


import androidx.lifecycle.ViewModel
import com.project.dessertapp.model.datasources.EventsProductsProvider
import com.project.dessertapp.model.entities.EventsProducts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EventsProductsViewModel : ViewModel() {

    // LiveData for EventProduct list
    private val _eventProducts = MutableStateFlow<List<EventsProducts>>(emptyList())
    val eventProducts: StateFlow<List<EventsProducts>> get() = _eventProducts

    // LiveData for filtered products by event
    private val _productsForEvent = MutableStateFlow<List<EventsProducts>>(emptyList())
    val productsForEvent: StateFlow<List<EventsProducts>> get() = _productsForEvent

    // LiveData for filtered events by product
    private val _eventsForProduct = MutableStateFlow<List<EventsProducts>>(emptyList())
    val eventsForProduct: StateFlow<List<EventsProducts>> get() = _eventsForProduct

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    init {
        // Initialize with all event-product relationships
        loadAllEventProducts()
    }

    // Load all event-product relationships
    fun loadAllEventProducts() {
        _eventProducts.value = EventsProductsProvider.findAllEventProducts()
    }

    // Load products for a specific event
    fun loadProductsForEvent(eventId: Long) {
        _productsForEvent.value = EventsProductsProvider.findProductsByEventId(eventId)
    }

    // Load events for a specific product
    fun loadEventsForProduct(productId: Long) {
        _eventsForProduct.value = EventsProductsProvider.findEventsByProductId(productId)
    }

    // Add a new event-product relationship
    fun addEventProduct(productId: Long, eventId: Long) {
        val newEventProduct = EventsProducts(productId = productId, eventId = eventId)
        EventsProductsProvider.addEventProduct(newEventProduct)
        loadAllEventProducts()  // Refresh the list after adding
    }

    // Remove an event-product relationship
    fun deleteEventProduct(productId: Long, eventId: Long) {
        EventsProductsProvider.deleteEventProduct(productId, eventId)
        loadAllEventProducts()  // Refresh the list after deletion
    }
}
