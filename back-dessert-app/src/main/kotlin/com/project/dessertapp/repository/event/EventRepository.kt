package com.project.dessertapp.repository.event

import com.project.dessertapp.entities.event.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<Event, Long>