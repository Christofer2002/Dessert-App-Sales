package com.project.dessertapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.dessertapp.model.entities.Event
import com.project.dessertapp.model.repository.EventRepository
import com.project.dessertapp.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * EventViewModel handles fetching and providing event data to the UI.
 */
@HiltViewModel
class EventViewModel @Inject constructor (
    private val eventRepository: EventRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    // MutableStateFlow that holds the list of events
    private val _eventList = MutableStateFlow<List<Event>>(emptyList())
    val eventList: StateFlow<List<Event>> get() = _eventList

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    // Function to find and set all events
    fun findAllEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val eventList = eventRepository.getAllEvents(tokenManager.getToken())
                _eventList.value = eventList ?: emptyList()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load events"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
