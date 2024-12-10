package com.project.dessertapp.model.entities

data class Role(
    val id: Long,
    val name: String,
    val privileges: List<Privilege>  // Lista de privilegios asociados al rol
)
