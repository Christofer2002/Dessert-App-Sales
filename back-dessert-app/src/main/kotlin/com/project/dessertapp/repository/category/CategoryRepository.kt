package com.project.dessertapp.repository.category

import com.project.dessertapp.entities.product.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long>