package com.project.dessertapp.entities.order

import com.project.dessertapp.entities.product.Product
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "orders_details")
data class OrderDetail(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(nullable = false)
    var amount: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    var order: Order? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    var product: Product
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as OrderDetail

        return id == other.id &&
                amount == other.amount &&
                order == other.order &&
                product == other.product
    }

    override fun hashCode(): Int {
        return Objects.hash(id, amount, order, product)
    }

    override fun toString(): String {
        return "OrderDetail(id=$id, amount=$amount, order=${order?.id}, product=${product.id})"
    }
}
