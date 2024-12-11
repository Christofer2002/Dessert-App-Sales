package com.project.dessertapp.entities.core

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "created_date", nullable = false)
    val createdDate: Date = Date(),

    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Column(name = "enabled", nullable = false)
    val enabled: Boolean = true,

    @Column(name = "first_name", nullable = false)
    val firstName: String,

    @Column(name = "last_name", nullable = false)
    val lastName: String,

    @Column(name = "password", nullable = false)
    val password: String? = null,

    @Column(name = "token_expired", nullable = false)
    val tokenExpired: Boolean = false,

    // Many-to-Many relationship with Role
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: Set<Role>? = null,
) {
    // Override equals, hashCode, and toString
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as User
        return id == other.id &&
                email == other.email &&
                enabled == other.enabled &&
                firstName == other.firstName &&
                lastName == other.lastName &&
                password == other.password &&
                tokenExpired == other.tokenExpired
    }

    override fun hashCode(): Int {
        return Objects.hash(id, email, enabled, firstName, lastName, password, tokenExpired)
    }

    override fun toString(): String {
        return "User(id=$id, createdDate=$createdDate, email='$email', enabled=$enabled, firstName='$firstName', lastName='$lastName', tokenExpired=$tokenExpired)"
    }
}
