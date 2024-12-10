package com.project.dessertapp.model.network.user

import com.google.gson.*
import com.project.dessertapp.model.entities.Authority
import com.project.dessertapp.model.entities.Role
import com.project.dessertapp.model.entities.User
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

/**
 * Custom deserializer for the User class to handle JSON deserialization.
 */
class UserDeserializer : JsonDeserializer<User> {

    // Date format used to parse the createdDate
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

    /**
     * Deserializes a JSON element into a User object.
     *
     * @param json The JSON element to deserialize.
     * @param typeOfT The type of the object to deserialize to.
     * @param context The context for deserialization.
     * @return The deserialized User object.
     * @throws JsonParseException If the JSON is not in the expected format.
     */
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): User {
        val jsonObject = json.asJsonObject

        // Extract the id (handles Long type)
        val id = try {
            jsonObject.get("id").asLong  // Attempt to get it as a long
        } catch (e: NumberFormatException) {
            jsonObject.get("id").asString.toLong()  // Fallback: Parse string to long
        }

        // Extract the createdDate (parse to Date)
        val createdDateStr = jsonObject.get("createdDate").asString
        val createdDate: Date = try {
            dateFormat.parse(createdDateStr) ?: Date()  // Parse the date string, fallback to current date if null
        } catch (e: Exception) {
            Date()  // If parsing fails, use the current date as a fallback
        }

        // Extract the other fields from the JSON object
        val email = jsonObject.get("email").asString
        val enabled = jsonObject.get("enabled").asBoolean
        val firstName = jsonObject.get("firstName").asString
        val lastName = jsonObject.get("lastName").asString
        val password = jsonObject.get("password").asString
        val tokenExpired = jsonObject.get("tokenExpired").asBoolean

        // Extract roles (a list of Role objects) using the context to deserialize
        /*val rolesArray = jsonObject.getAsJsonArray("authorities")
        val roles: List<Authority> = rolesArray.map { roleElement ->
            context.deserialize<Authority>(roleElement, Authority::class.java)
        }*/

        // Return the fully deserialized User object
        return User(
            id = id,
            createdDate = createdDate,
            email = email,
            enabled = enabled,
            firstName = firstName,
            lastName = lastName,
            password = password,
            tokenExpired = tokenExpired,
        )
    }
}
