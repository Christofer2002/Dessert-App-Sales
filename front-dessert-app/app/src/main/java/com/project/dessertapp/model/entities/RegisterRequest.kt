package com.project.dessertapp.model.entities

data class RegisterRequest(
    val userName: String,
    val lastName: String,
    val email: String,
    val tastes: List<String>,
    val password: String)