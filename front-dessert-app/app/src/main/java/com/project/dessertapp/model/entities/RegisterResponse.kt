package com.project.dessertapp.model.entities

data class RegisterResponse(
    val userName: String,
    val lastName: String,
    val email: String,
    val tastes: List<String>,
    val password: String)
{

}