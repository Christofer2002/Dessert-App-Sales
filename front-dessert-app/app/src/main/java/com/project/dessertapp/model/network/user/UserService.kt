package com.project.dessertapp.model.network.user

import com.project.dessertapp.model.entities.LoginRequest
import com.project.dessertapp.model.entities.LoginResponse
import com.project.dessertapp.model.entities.RegisterRequest
import com.project.dessertapp.model.entities.RegisterResponse
import com.project.dessertapp.model.entities.Taste
import com.project.dessertapp.model.entities.User
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    @POST("users/signup")
    suspend fun register(
        @Body user: User
    ): Response<User>

    @POST("tastes")
    suspend fun saveTastes(
        @Query("userId") userId: Long,
        @Body tastes: List<Taste>
    ): Response<Void>

    @POST("users/login")  // Route for user authentication
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("user-profile")  // Fetch user profile by ID
    suspend fun getUserProfile(
        @Header("Authorization") token: String?,
        @Query("username") username: String
    ): Response<User>

    @PUT("users/{id}")  // Update user profile
    suspend fun updateUserProfile(
        @Path("id") userId: Long,
        @Body user: User
    ): Response<User>

    @GET("tastes")  // Define a route to fetch the user's tastes
    suspend fun getTastes(): Response<List<Taste>> // The response is a list of strings

}
