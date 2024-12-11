package com.project.dessertapp.repository.event

import com.project.dessertapp.entities.associations.EventProduct
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventProductRepository : JpaRepository<EventProduct, Long> {
    fun findByEvent_Id(eventId: Long): List<EventProduct>
}