package com.project.dessertapp.model.network.product

import com.google.gson.*
import com.project.dessertapp.model.entities.Product
import java.lang.reflect.Type

/**
 * Custom deserializer for the Task class to handle JSON deserialization.
 */
class ProductDeserializer : JsonDeserializer<Product> {
    /**
     * Deserializes a JSON element into a Task object.
     *
     * @param json The JSON element to deserialize.
     * @param typeOfT The type of the object to deserialize to.
     * @param context The context for deserialization.
     * @return The deserialized Product object.
     * @throws JsonParseException If the JSON is not in the expected format.
     */
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Product {
        val jsonObject = json.asJsonObject

        // Extract the id, handling both long and string formats
        val id = try {
            jsonObject.get("id").asLong  // Try to get it as an integer
        } catch (e: NumberFormatException) {
            jsonObject.get("id").asString.toLong()  // Fallback: Parse the string to integer
        }

        // Extract other fields from the JSON object
        val name = jsonObject.get("name").asString
        val flavour = jsonObject.get("flavour").asString
        val durationDays = jsonObject.get("durationDays").asInt
        val unitPrice = jsonObject.get("unitPrice").asDouble
        val description = jsonObject.get("description").asString
        val image = jsonObject.get("image").asString
        val categoryId = jsonObject.get("categoryId").asLong

        return Product(id, name, flavour, durationDays, unitPrice, description, image, categoryId)
    }
}