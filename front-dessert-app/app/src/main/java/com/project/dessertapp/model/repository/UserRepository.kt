package com.project.dessertapp.model.repository

import android.util.Log
import com.project.dessertapp.model.entities.LoginRequest
import com.project.dessertapp.model.entities.LoginResponse
import com.project.dessertapp.model.entities.RegisterRequest
import com.project.dessertapp.model.entities.RegisterResponse
import com.project.dessertapp.model.entities.Taste
import com.project.dessertapp.model.entities.User
import com.project.dessertapp.model.network.user.UserService
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService
) {
    /**
     * Logs in a user with the provided credentials.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return A Pair containing the LoginResponse object and the JWT token, or null if an error occurs.
     */
    suspend fun login(username: String, password: String): Result<Pair<LoginResponse?, String?>> {
        return try {
            val response: Response<LoginResponse> =
                userService.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                // Extract the JWT token from the header (assuming it's in the "Authorization" header)
                val token = response.headers()["Authorization"]?.replace("Bearer ", "")
                Result.success(Pair(response.body(), token))
            } else {
                Result.failure(Exception("Login failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(user: User, tastes: List<Taste>): Result<User?> {
        return try {
            val response: Response<User> =
                userService.register(user)
            if (response.isSuccessful) {
                response.body()?.id?.let { userService.saveTastes(it, tastes) }

                // Extract the JWT token from the header (assuming it's in the "Authorization" header)
                /*
                * Pending to check to know if the token is needed
                * */
                //val token = response.headers()["Authorization"]?.replace("Bearer ", "")
                Result.success(response.body())

            } else {
                Result.failure(Exception("Login failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Fetches the user's profile by their ID.
     * @param token The JWT token of the user.
     * @return The User object if successful, or null if it fails.
     */
    suspend fun getUser(token: String?, username: String): User? {
        return safeApiCall { userService.getUserProfile(token, username) }
    }

    /**
     * Fetches the user's tastes from the API.
     *
     * @return A Result containing the list of tastes, or an exception if an error occurs.
     */
    suspend fun getTastes(): Result<List<Taste>> {
        return try {
            // Make the API call to fetch the user's tastes
            val response = userService.getTastes()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList()) // Return the list of tastes
            } else {
                Result.failure(Exception("Failed to fetch tastes: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * A generic function to safely handle API calls.
     * @param apiCall A suspend lambda that makes the API call and returns a Response<T>.
     * @return The result object if successful, or null if any error occurs.
     */
    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): T? {
        return try {
            val response = apiCall.invoke()
            if (response.isSuccessful) {
                response.body()  // Return the response body if successful
            } else {
                Log.e("UserRepository", "API call failed: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Exception during API call: ${e.message}")
            null
        }
    }
}
