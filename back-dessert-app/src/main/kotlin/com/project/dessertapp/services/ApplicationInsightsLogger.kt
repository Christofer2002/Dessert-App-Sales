package com.project.dessertapp.services

import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class ApplicationInsightsLogger {

    //MS Account private val instrumentationKey = "58933e83-5951-48a2-a133-c0c5e0a0f4e9"  // Reemplaza con tu clave de instrumentación
    private val instrumentationKey = "64ca5afc-5f7d-4c52-abd4-ece742980be4"  // Reemplaza con tu clave de instrumentación
    private val url = "https://dc.services.visualstudio.com/v2/track"  // URL de ingestión de datos

    fun logEvent(eventName: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val formattedDate = dateFormat.format(Date())

        val jsonPayload = """
            {
              "name": "Microsoft.ApplicationInsights.Event",
              "time": "$formattedDate",
              "iKey": "$instrumentationKey",
              "data": {
                "baseType": "EventData",
                "baseData": {
                  "name": "$eventName",
                  "properties": {
                    "customProperty": "customValue"
                  }
                }
              },
              "tags": {},
              "ver": 1
            }
        """.trimIndent()

        println("JSON Payload: $jsonPayload")

        try {
            val urlObj = URL(url)
            val connection = urlObj.openConnection() as HttpURLConnection

            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.doOutput = true

            connection.outputStream.use { os ->
                val input = jsonPayload.toByteArray()
                os.write(input, 0, input.size)
            }

            val responseCode = connection.responseCode
            println("Response Code: $responseCode")

            if (responseCode != HttpURLConnection.HTTP_OK) {
                val errorResponse = connection.errorStream.bufferedReader().readText()
                println("Error Response: $errorResponse")
            }

            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

