package com.project.dessertapp.entities.core

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "privilege")
data class Privilege(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "name", nullable = false)
    val name: String
) {
    // Override equals, hashCode, and toString
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as Privilege
        return id == other.id && name == other.name
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name)
    }

    override fun toString(): String {
        return "Privilege(id=$id, name='$name')"
    }
}
