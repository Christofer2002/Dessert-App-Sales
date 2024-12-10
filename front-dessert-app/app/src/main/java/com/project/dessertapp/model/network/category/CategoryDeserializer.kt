package com.project.dessertapp.model.network.category

import com.google.gson.*
import com.project.dessertapp.model.entities.Category
import java.lang.reflect.Type

/**
 * Custom deserializer for the Task class to handle JSON deserialization.
 */
class CategoryDeserializer : JsonDeserializer<Category> {
    /**
     * Deserializes a JSON element into a Task object.
     *
     * @param json The JSON element to deserialize.
     * @param typeOfT The type of the object to deserialize to.
     * @param context The context for deserialization.
     * @return The deserialized Category object.
     * @throws JsonParseException If the JSON is not in the expected format.
     */
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Category {
        val jsonObject = json.asJsonObject

        // Extract the id, handling both long and string formats
        val id = try {
            jsonObject.get("id").asLong  // Try to get it as an integer
        } catch (e: NumberFormatException) {
            jsonObject.get("id").asString.toLong()  // Fallback: Parse the string to integer
        }

        // Extract other fields from the JSON object
        val name = jsonObject.get("name").asString
        val image = jsonObject.get("image").asString

        return Category(id, name, image)
    }
}