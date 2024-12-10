package com.project.dessertapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.dessertapp.model.entities.Taste
import com.project.dessertapp.model.entities.User
import com.project.dessertapp.model.repository.UserRepository
import com.project.dessertapp.model.state.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
data class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel(){

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> get() = _loginError

    private val _user = MutableStateFlow<UserState?>(null)
    val user: StateFlow<UserState?> get() = _user

    private val _tastes = MutableStateFlow<List<Taste>?>(null)
    val tastes: StateFlow<List<Taste>?> get() = _tastes


    fun register(user : User, tastes: List<Taste>) {
        viewModelScope.launch {
            val result = userRepository.register(user, tastes)
            result.onSuccess { registerResponse ->
                if (registerResponse != null) {
                    // Store the JWT token via the TokenManager after successful login

                    _isLoggedIn.value = true
                    _loginError.value = null
                } else {
                    _isLoggedIn.value = false
                    _loginError.value = "Registration failed. Please check your credentials."
                }
            }.onFailure { exception ->
                _isLoggedIn.value = false
                _loginError.value = "An error occurred: ${exception.message}"
            }
        }
    }

    fun setError(message: String) {
        _loginError.value = message
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        _isLoggedIn.value = isLoggedIn
    }

    /**
     * Function to get the tastes
     */
    fun getTastes() {
        viewModelScope.launch {
            val result = userRepository.getTastes()
            result.onSuccess { tastes ->
                _tastes.value = tastes
            }
        }
    }
}

