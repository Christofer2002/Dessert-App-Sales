package com.project.dessertapp.entities.associations

import com.project.dessertapp.entities.core.User
import com.project.dessertapp.entities.product.Taste
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "user_tastes")
data class UserTastes(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tastes_id", nullable = false)
    var taste: Taste
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as UserTastes

        return id == other.id &&
                user == other.user &&
                taste == other.taste
    }

    override fun hashCode(): Int {
        return Objects.hash(id, user, taste)
    }

    override fun toString(): String {
        return "UserTastes(id=$id, user=${user.id}, taste=${taste.id})"
    }
}