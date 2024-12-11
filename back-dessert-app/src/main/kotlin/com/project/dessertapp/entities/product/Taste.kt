package com.project.dessertapp.entities.product

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "tastes")
data class Taste(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    var name: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as Taste

        return id == other.id &&
                name == other.name
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name)
    }

    override fun toString(): String {
        return "Taste(id=$id, name='$name')"
    }

}