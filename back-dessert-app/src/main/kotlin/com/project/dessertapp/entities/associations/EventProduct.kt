package com.project.dessertapp.entities.associations

import com.project.dessertapp.entities.product.Product
import com.project.dessertapp.entities.event.Event
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "events_products")
data class EventProduct(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    var product: Product,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = false)
    var event: Event
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as EventProduct

        return id == other.id &&
                product == other.product &&
                event == other.event
    }

    override fun hashCode(): Int {
        return Objects.hash(id, product, event)
    }

    override fun toString(): String {
        return "EventProduct(id=$id, product=${product.id}, event=${event.id})"
    }
}
