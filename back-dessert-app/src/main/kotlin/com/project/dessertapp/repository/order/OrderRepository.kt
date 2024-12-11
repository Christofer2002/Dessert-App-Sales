package com.project.dessertapp.repository.order

import com.project.dessertapp.entities.event.Event
import com.project.dessertapp.entities.order.Order
import com.project.dessertapp.entities.order.OrderDetail
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long>

@Repository
interface OrderDetailRepository : JpaRepository<OrderDetail, Long> {
    fun findByOrder(order: Order): List<OrderDetail>
}