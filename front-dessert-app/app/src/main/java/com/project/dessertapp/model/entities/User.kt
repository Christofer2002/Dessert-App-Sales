package com.project.dessertapp.model.entities

import java.util.Date

data class User (
    val id: Long? = null,
    val createdDate: Date? = null,
    val email: String,
    val enabled: Boolean = true,
    val firstName: String,
    val lastName: String,
    val password: String = "",
    val tokenExpired: Boolean = false,
)