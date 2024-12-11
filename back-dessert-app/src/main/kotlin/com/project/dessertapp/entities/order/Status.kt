package com.project.dessertapp.entities.order

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "status")
data class Status(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var label: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as Status

        return id == other.id &&
                label == other.label
    }

    override fun hashCode(): Int {
        return Objects.hash(id, label)
    }

    override fun toString(): String {
        return "Status(id=$id, label='$label')"
    }

}
