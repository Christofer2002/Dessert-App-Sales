package com.project.dessertapp.entities.product

import jakarta.persistence.*
import java.util.Objects

@Entity
@Table(name = "categories")
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var name: String? = null,

    @Column(nullable = false)
    var image: String? = null
) {

    // Sobrescribir el método equals
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as Category
        return id == other.id &&
                name == other.name &&
                image == other.image
    }

    // Sobrescribir el método hashCode
    override fun hashCode(): Int {
        return Objects.hash(id, name, image)
    }

    // Sobrescribir el método toString
    override fun toString(): String {
        return "Category(id=$id, name='$name', image='$image')"
    }
}
