package com.project.dessertapp.repository.product

import com.project.dessertapp.entities.product.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findByCategoryId(categoryId: Long): List<Product>
}
