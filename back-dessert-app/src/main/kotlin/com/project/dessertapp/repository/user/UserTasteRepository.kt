package com.project.dessertapp.repository.user

import com.project.dessertapp.entities.associations.UserTastes
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserTasteRepository : JpaRepository<UserTastes, Long> {
}
