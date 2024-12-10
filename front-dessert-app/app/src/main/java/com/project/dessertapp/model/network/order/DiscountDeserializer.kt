package com.project.dessertapp.model.network.order

import com.google.gson.*
import com.project.dessertapp.model.entities.Discount
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

/**
 * Custom deserializer for the Discount class to handle JSON deserialization.
 */
class DiscountDeserializer : JsonDeserializer<Discount> {

    // Date format to parse the valid_from and valid_to (assumes date in 'yyyy-MM-dd' format)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    /**
     * Deserializes a JSON element into a Discount object.
     */
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Discount {
        val jsonObject = json.asJsonObject

        // Extract the id
        val id = jsonObject.get("id").asLong

        // Extract the code
        val code = jsonObject.get("code").asString

        // Extract the description
        val description = jsonObject.get("description").asString

        // Extract the discount_percentage
        val discountPercentage = jsonObject.get("discountPercentage").asDouble
        // Extract and parse the valid_from date
        val validFromStr = jsonObject.get("validFrom").asString
        val validFrom: Date = try {
            dateFormat.parse(validFromStr) ?: Date()
        } catch (e: Exception) {
            Date()
        }

        // Extract and parse the valid_to date
        val validToStr = jsonObject.get("validTo").asString
        val validTo: Date = try {
            dateFormat.parse(validToStr) ?: Date()
        } catch (e: Exception) {
            Date()
        }

        // Return the fully deserialized Discount object
        return Discount(
            id = id,
            code = code,
            description = description,
            discountPercentage = discountPercentage,
            validFrom = validFrom,
            validTo = validTo
        )
    }
}