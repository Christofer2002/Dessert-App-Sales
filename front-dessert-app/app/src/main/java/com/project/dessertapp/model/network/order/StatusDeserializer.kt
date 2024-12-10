package com.project.dessertapp.model.network.order

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.project.dessertapp.model.entities.Discount
import com.project.dessertapp.model.entities.Status
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Custom deserializer for the Status class to handle JSON deserialization.
 */
class StatusDeserializer : JsonDeserializer<Status> {
    /**
     * Deserializes a JSON element into a Status object.
     */
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Status {
        val jsonObject = json.asJsonObject

        // Extract the id
        val id = jsonObject.get("id").asLong

        // Return the fully deserialized Discount object
        return Status.fromId(id) ?: throw JsonParseException("Unknown Status ID: $id")
    }
}