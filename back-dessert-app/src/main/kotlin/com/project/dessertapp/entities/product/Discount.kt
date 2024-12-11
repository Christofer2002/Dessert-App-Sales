package com.project.dessertapp.entities.product

import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "discounts")
data class Discount(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var code: String,

    @Column(nullable = false)
    var description: String,

    @Column(name = "discount_percentage", nullable = false)
    var discountPercentage: Double,

    @Column(name = "valid_from", nullable = false)
    var validFrom: LocalDate,

    @Column(name = "valid_to", nullable = false)
    var validTo: LocalDate
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as Discount

        return id == other.id &&
                code == other.code &&
                description == other.description &&
                discountPercentage == other.discountPercentage &&
                validFrom == other.validFrom &&
                validTo == other.validTo
    }

    override fun hashCode(): Int {
        return Objects.hash(id, code, description, discountPercentage, validFrom, validTo)
    }

    override fun toString(): String {
        return "Discount(id=$id, code='$code', description='$description', discountPercentage=$discountPercentage, validFrom=$validFrom, validTo=$validTo)"
    }

}
