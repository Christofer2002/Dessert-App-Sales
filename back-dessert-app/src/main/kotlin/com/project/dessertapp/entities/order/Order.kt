package com.project.dessertapp.entities.order

import com.project.dessertapp.entities.core.User
import com.project.dessertapp.entities.product.Discount
import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "orders")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "order_date", nullable = false)
    var orderDate: Date,

    @Column(name = "deliver_date", nullable = false)
    var deliverDate: Date,

    @Column(name = "total_price", nullable = false)
    var totalPrice: Double,

    @Column(name = "delivery_instructions")
    var deliveryInstructions: String,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
    var status: Status,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id", referencedColumnName = "id", nullable = true)
    var discount: Discount? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as Order

        return id == other.id &&
                orderDate == other.orderDate &&
                deliverDate == other.deliverDate &&
                totalPrice == other.totalPrice &&
                deliveryInstructions == other.deliveryInstructions &&
                user == other.user &&
                status == other.status &&
                discount == other.discount
    }

    override fun hashCode(): Int {
        return Objects.hash(id, orderDate, deliverDate, totalPrice, deliveryInstructions, user, status, discount)
    }

    override fun toString(): String {
        return "Order(id=$id, orderDate=$orderDate, deliverDate=$deliverDate, totalPrice=$totalPrice, deliveryInstructions='$deliveryInstructions', user=${user.id}, status=${status.id}, discount=${discount?.id})"
    }

}