package com.project.dessertapp.entities

import jakarta.persistence.*
import java.util.Objects

@Entity
@Table(name = "products")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var flavour: String,

    @Column(nullable = false)
    var durationDays: Int,

    @Column(nullable = false)
    var unitPrice: Double,

    @Column(nullable = false, length = 500)
    var description: String,

    @Column(nullable = false)
    var image: String,

    // Relación con la entidad Category (muchos productos a una categoría)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category
) {

    // Sobrescribir equals
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as Product
        return id == other.id &&
                name == other.name &&
                flavour == other.flavour &&
                durationDays == other.durationDays &&
                unitPrice == other.unitPrice &&
                description == other.description &&
                image == other.image &&
                category == other.category
    }

    // Sobrescribir hashCode
    override fun hashCode(): Int {
        return Objects.hash(id, name, flavour, durationDays, unitPrice, description, image, category)
    }

    // Sobrescribir toString
    override fun toString(): String {
        return "Product(id=$id, name='$name', flavour='$flavour', durationDays=$durationDays, unitPrice=$unitPrice, description='$description', image='$image', category=${category.id})"
    }
}
