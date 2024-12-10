package com.project.dessertapp.model.repository

import android.util.Log
import com.project.dessertapp.model.entities.Discount
import com.project.dessertapp.model.entities.Order
import com.project.dessertapp.model.entities.OrderDetails
import com.project.dessertapp.model.entities.Status
import com.project.dessertapp.model.network.order.OrderService
import com.project.dessertapp.model.state.OrderWithDetails
import retrofit2.Response
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderService: OrderService // Inyectamos el servicio de órdenes
) {

    /**
     * Fetches all orders from the API and returns them as a list.
     *
     * @return A list of Order objects, or an empty list if an error occurs.
     */
    suspend fun getAllOrders(token : String?): List<Order> {
        return safeApiCallList { orderService.getAllOrders(token) }
    }

    /**
     * Fetches an order by its ID from the API.
     *
     * @param id The ID of the order to fetch.
     * @return The Order object or null if an error occurs.
     */
    suspend fun getOrderById(id: Long, token : String?): Order? {
        return safeApiCall { orderService.getOrderById(id, token) }
    }

    /**
     * Fetches the order details for a given order ID.
     *
     * @param orderId The ID of the order to fetch details for.
     * @return A list of OrderDetails objects, or an empty list if an error occurs.
     */
    suspend fun getOrderDetailsByOrderId(orderId: Long, token : String?): List<OrderDetails> {
        return safeApiCallList { orderService.getOrderDetailsByOrderId(orderId, token) }
    }

    /**
     * Adds a new order to the API and returns the added order.
     *
     * @param order The Order object to add.
     * @return The added Order object or null if an error occurs.
     */
    suspend fun addOrderWithDetails(orderWithDetails: OrderWithDetails, token: String?): Order? {
        return safeApiCall { orderService.addOrderWithDetails(orderWithDetails, token) }
    }

    /**
     * Updates the status of an order by its ID.
     *
     * @param orderId The ID of the order to update.
     * @param newStatus The new StatusDTO object to set.
     * @param token The authorization token.
     * @return True if the update was successful, false otherwise.
     */
    suspend fun updateOrderStatus(orderId: Long, newStatus: Long, token: String?): Boolean {
        return try {
            val response = orderService.updateOrderStatus(orderId, newStatus, token)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("OrderRepository", "Failed to update order status: ${e.message}")
            false
        }
    }

    /**
     * A generic function to safely handle API calls that return lists.
     * @param apiCall A suspend lambda that returns a Response<List<T>>.
     * @return A list of results if successful or an empty list if an error occurs.
     */
    private suspend fun <T> safeApiCallList(apiCall: suspend () -> Response<List<T>>): List<T> {
        return try {
            val response = apiCall.invoke()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("OrderRepository", "API call failed: ${e.message}") // Añade logs
            emptyList()
        }
    }

    /**
     * A generic function to safely handle API calls that return a single object.
     * @param apiCall A suspend lambda that returns a Response<T>.
     * @return The result object if successful or null if an error occurs.
     */
    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): T? {
        return try {
            val response = apiCall.invoke()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getDiscountByCode(code: String, token : String?): Discount? {
        return safeApiCall { orderService.getDiscountByCode(code, token) }
    }
}
