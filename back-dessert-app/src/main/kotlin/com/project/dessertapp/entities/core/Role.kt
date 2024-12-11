package com.project.dessertapp.entities.core

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "role")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "name", nullable = false)
    val name: String,

    // Many-to-Many relationship with Privilege
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_privilege",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "privilege_id")]
    )
    val privileges: Set<Privilege> = emptySet()
) {
    // Override equals, hashCode, and toString
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as Role
        return id == other.id && name == other.name
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name)
    }

    override fun toString(): String {
        return "Role(id=$id, name='$name')"
    }
}
