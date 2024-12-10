package com.project.dessertapp.model.network.order

import com.google.gson.*
import com.project.dessertapp.model.entities.Discount
import com.project.dessertapp.model.entities.Order
import com.project.dessertapp.model.entities.Status
import com.project.dessertapp.model.entities.User
import com.project.dessertapp.model.state.StatusDTO
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

/**
 * Custom deserializer for the Order class to handle JSON deserialization.
 */
class OrderDeserializer : JsonDeserializer<Order> {

    // Date format to parse the orderDate and deliverDate (assumes date in 'yyyy-MM-dd' format)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    /**
     * Deserializes a JSON element into an Order object.
     */
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Order {
        val jsonObject = json.asJsonObject

        // Extract the id
        val id = jsonObject.get("id").asLong

        // Extract and parse the orderDate
        val orderDateStr = jsonObject.get("orderDate").asString
        val orderDate: Date = try {
            dateFormat.parse(orderDateStr) ?: Date()
        } catch (e: Exception) {
            Date()
        }

        // Extract and parse the deliverDate
        val deliverDateStr = jsonObject.get("deliverDate").asString
        val deliverDate: Date = try {
            dateFormat.parse(deliverDateStr) ?: Date()
        } catch (e: Exception) {
            Date()
        }

        // Extract the totalPrice
        val totalPrice = jsonObject.get("totalPrice").asDouble

        // Extract the deliveryInstructions
        val deliveryInstructions = jsonObject.get("deliveryInstructions").asString

        // Extract the userId
        val user = jsonObject.get("user")?.let {
            context.deserialize<User>(it, User::class.java)
        } ?: throw IllegalArgumentException("User cannot be null")

        // Extract the status object and map it to the enum Status
        val status = jsonObject.get("status")?.asJsonObject?.let {
            val statusId = it.get("id").asLong
            val statusLabel = it.get("label").asString

            // Map to StatusDTO if you have a separate DTO
            StatusDTO(id = statusId, label = statusLabel)
        } ?: throw IllegalArgumentException("Status cannot be null")

        // Extract the discount object, allowing null
        val discount = if (jsonObject.has("discount") && !jsonObject.get("discount").isJsonNull) {
            context.deserialize<Discount>(jsonObject.get("discount"), Discount::class.java)
        } else {
            null
        }

        // Return the fully deserialized Order object
        return Order(
            id = id,
            orderDate = orderDate,
            deliverDate = deliverDate,
            totalPrice = totalPrice,
            deliveryInstructions = deliveryInstructions,
            user = user,            // Use the deserialized User object
            status = status,         // Use the deserialized Status object
            discount = discount      // Use the deserialized Discount object, which may be null
        )
    }
}
