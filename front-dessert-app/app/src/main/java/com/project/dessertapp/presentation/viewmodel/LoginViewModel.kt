package com.project.dessertapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.dessertapp.model.entities.Authority
import com.project.dessertapp.model.entities.LoginResponse
import com.project.dessertapp.model.entities.OrderDetails
import com.project.dessertapp.model.entities.Taste
import com.project.dessertapp.model.entities.User
import com.project.dessertapp.model.repository.UserRepository
import com.project.dessertapp.model.state.UserState
import com.project.dessertapp.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * LoginViewModel handles the login process and provides the logged-in user data to the UI.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    // StateFlow to hold the login status
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn

    // StateFlow to hold the error message (for login failures)
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> get() = _loginError

    private val _user = MutableStateFlow<User?>(null) // Inicializa con null
    val user: StateFlow<User?> get() = _user

    private val _userResponse = MutableStateFlow<LoginResponse?>(null)
    val userResponse: StateFlow<LoginResponse?> get() = _userResponse

    /**
     * Function to handle user login.
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = userRepository.login(username, password)
            result.onSuccess { (loginResponse, token) ->
                if (loginResponse != null && token != null) {
                    // Store the JWT token via the TokenManager after successful login
                    tokenManager.saveToken(token)

                    _userResponse.value = loginResponse

                    /*_user.value = User(
                        loginResponse.id,
                        loginResponse.createdDate,
                        loginResponse.username,
                        loginResponse.enabled,
                        loginResponse.firstName,
                        loginResponse.lastName,
                        loginResponse.password,
                        loginResponse.accountNonExpired,
                        loginResponse.authorities
                    )*/

                    _isLoggedIn.value = true
                    _loginError.value = null
                } else {
                    _isLoggedIn.value = false
                    _loginError.value = "Login failed. Please check your credentials."
                }
            }.onFailure { exception ->
                _isLoggedIn.value = false
                _loginError.value = "An error occurred: ${exception.message}"
            }
        }
    }

    /**
     * Function to handle logout logic.
     */
    fun logout() {
        tokenManager.clearToken() // Clear the token via TokenManager
        _isLoggedIn.value = false
        _user.value = null
    }

    /**
     * Check if the user has the Admin role.
     */
    fun isAdmin(): Boolean {
        return userResponse.value?.authorities?.any { it.authority == "ROLE_ADMIN" } ?: false
    }
}
