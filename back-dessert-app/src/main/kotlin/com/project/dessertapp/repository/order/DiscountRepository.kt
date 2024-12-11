package com.project.dessertapp.repository.order

import com.project.dessertapp.entities.product.Discount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DiscountRepository : JpaRepository<Discount, Long> {
    fun findByCode(code: String): Optional<Discount>
}
