package com.project.dessertapp.repository.product

import com.project.dessertapp.entities.product.Taste
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TasteRepository : JpaRepository<Taste, Long>