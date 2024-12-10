package com.project.dessertapp.model.network.order

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.project.dessertapp.model.entities.Order
import com.project.dessertapp.model.entities.OrderDetails
import com.project.dessertapp.model.entities.Product
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderDetailDeserializer : JsonDeserializer<OrderDetails> {

    /**
     * Deserializes a JSON element into an OrderDetails object.
     */
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): OrderDetails {
        val jsonObject = json.asJsonObject

        val id = jsonObject?.get("id")?.asLong ?: 0L
        val amount = jsonObject?.get("amount")?.asInt ?: 0

        val order = jsonObject.get("order")?.let {
            context.deserialize<Order>(it, Order::class.java)
        } ?: throw IllegalArgumentException("Order cannot be null")

        val product = jsonObject.get("product")?.let {
            context.deserialize<Product>(it, Product::class.java)
        } ?: throw IllegalArgumentException("Product cannot be null")

        // Return the fully deserialized OrderDetails object
        return OrderDetails(
            id = id,
            amount = amount,
            order = order,
            product = product
        )
    }
}
