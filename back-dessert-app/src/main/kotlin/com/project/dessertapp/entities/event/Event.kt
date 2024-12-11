package com.project.dessertapp.entities.event

import com.project.dessertapp.entities.product.Product
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "events")
data class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var image: String,

    @Column(nullable = false)
    var description: String,

    // Relación Many-to-Many con Product
    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinTable(
        name = "events_products",
        joinColumns = [JoinColumn(name = "event_id")],
        inverseJoinColumns = [JoinColumn(name = "product_id")]
    )
    var products: Set<Product> = HashSet()  // Utiliza un Set para evitar duplicados
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as Event

        return id == other.id &&
                name == other.name &&
                image == other.image &&
                description == other.description
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name, image, description)
    }

    override fun toString(): String {
        return "Event(id=$id, name='$name', image='$image', description='$description')"
    }
}
