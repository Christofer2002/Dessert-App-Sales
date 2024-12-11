package com.project.dessertapp.repository.order

import com.project.dessertapp.entities.order.Status
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StatusRepository : JpaRepository<Status, Long>