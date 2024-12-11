package com.project.dessertapp.entities.product

import com.project.dessertapp.entities.event.Event
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "products")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

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

    @Column(nullable = false)
    var categoryId: Long?=null,

    // Relation Many-to-Many con Event
    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    //var events: Set<Event>? = HashSet()
    var events: Set<Event>? = null// Use HashSet to avoid duplicates
) {

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
                categoryId == other.categoryId
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name, flavour, durationDays, unitPrice, description, image, categoryId)
    }

    override fun toString(): String {
        return "Product(id=$id, name='$name', flavour='$flavour', durationDays=$durationDays, unitPrice=$unitPrice, description='$description', image='$image', categoryId=$categoryId)"
    }
}
