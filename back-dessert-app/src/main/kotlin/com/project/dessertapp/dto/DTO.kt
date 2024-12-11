package com.project.dessertapp.dto

import java.time.LocalDate
import java.util.*

data class PrivilegeDTO(
    val id: Long,
    val name: String
)

data class RoleDTO(
    val id: Long,
    val name: String
)

data class UserDTO(
    val id: Long? = null,
    val createdDate: Date? = Date(),
    val email: String,
    val enabled: Boolean? = true,
    val firstName: String,
    val lastName: String,
    val password: String? = "",
    val tokenExpired: Boolean? = false,
)

data class UserDTOResult(
    val id: Long? = null,
    val createdDate: Date? = Date(),
    val email: String,
    val enabled: Boolean,
    val firstName: String,
    val lastName: String,
    val password: String? = "",
    val tokenExpired: Boolean
)

data class UserForOrderDTO(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String
)

data class UserLoginInput(
    var username: String = "",
    var password: String = "",
)

data class EventDTO(
    var id: Long?=null,
    var name: String?=null,
    var image: String?=null,
    var description: String?=null
)

data class OrderDTO(
    var id: Long?=null,
    var orderDate: Date?=null,
    var deliverDate: Date?=null,
    var totalPrice: Double?=null,
    var deliveryInstructions: String?=null,
    var user: UserDTO?=null,
    var status: StatusDTO?=null,
    var discount: DiscountDTO? = null
)

data class OrderDetailsDTO(
    var id: Long?=null,
    var amount: Int,
    var order: OrderDTO? = null,
    var product: ProductDTO
)

data class StatusDTO(
    var id: Long,
    var label: String
)

data class CategoryDTO(
    var id: Long?=null,
    var name: String?=null,
    var image: String?=null
)

data class CategoryDTOResult(
    var id: Long,
    var name: String,
    var image: String
)

data class DiscountDTO(
    var id: Long?=null,
    var code: String?=null,
    var description: String?=null,
    var discountPercentage: Double?=null,
    var validFrom: LocalDate?=null,
    var validTo: LocalDate?=null
)

data class ProductDTO(
    var id: Long?=null,
    var name: String?=null,
    var flavour: String?=null,
    var durationDays: Int?=null,
    var unitPrice: Double?=null,
    var description: String?=null,
    var image: String?=null,
    var categoryId: Long?=null
)

data class ProductDTOResult(
    var id: Long,
    var name: String,
    var flavour: String,
    var durationDays: Int,
    var unitPrice: Double,
    var description: String,
    var image: String,
    var categoryId: Long
)

data class TasteDTO(
    var id: Long?=null,
    var name: String?=null
)

data class TasteDTOResult(
    var id: Long,
    var name: String
)

data class UserTastesDTO(
    val id: Long?=null,
    val user: UserDTO?=null,
    val taste: TasteDTO?=null
)

data class EventProductDTO(
    var id: Long,
    var product: ProductDTO,
    var event: EventDTO
)

data class OrderWithDetailsDTO(
    val order: OrderDTO,
    val orderDetails: List<OrderDetailsDTO>
)