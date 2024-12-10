package com.project.dessertapp.model.entities

enum class Status(val id: Long, val label: String) {
    WAITING(1, "Waiting"),
    ACCEPTED(2, "Accepted"),
    REJECTED(3, "Rejected");

    companion object {
        // Function to get the status from the ID
        fun fromId(id: Long): Status? {
            return values().find { it.id == id }
        }
    }
}
