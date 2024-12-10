package com.project.dessertapp.model.network.order

import com.project.dessertapp.model.entities.Discount
import com.project.dessertapp.model.entities.Order
import com.project.dessertapp.model.entities.OrderDetails
import com.project.dessertapp.model.entities.Status
import com.project.dessertapp.model.state.OrderWithDetails
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderService {

    /**
     * Fetches all orders from the API.
     *
     * @return A Response object containing a list of orders.
     * @param token The authorization token.
     */
    @GET("orders")
    suspend fun getAllOrders(
        @Header("Authorization") token: String?  // Authorization header with token
    ): Response<List<Order>>

    /**
     * Fetches an order by its ID from the API.
     *
     * @param id The ID of the order to fetch.
     * @param token The authorization token.
     * @return A Response object containing the order.
     */
    @GET("orders/{id}")
    suspend fun getOrderById(
        @Path("id") id: Long,  // ID of the order to fetch
        @Header("Authorization") token: String?  // Authorization header with token
    ): Response<Order>

    /**
     * Fetches the details of an order by its ID.
     *
     * @param orderId The ID of the order whose details are fetched.
     * @param token The authorization token.
     * @return A Response object containing a list of order details.
     */
    @GET("orders/details")
    suspend fun getOrderDetailsByOrderId(
        @Query("id") orderId: Long,  // ID of the order to fetch details for
        @Header("Authorization") token: String?  // Authorization header with token
    ): Response<List<OrderDetails>>

    /**
     * Adds a new order to the API.
     *
     * @param order The Order object to add.
     * @param token The authorization token.
     * @return A Response object containing the added order.
     */
    @POST("orders/createWithDetails")
    suspend fun addOrderWithDetails(
        @Body orderWithDetails: OrderWithDetails,  // the composite object
        @Header("Authorization") token: String?  // Token
    ): Response<Order>

    /**
     * Fetches a discount by its code from the API.
     *
     * @param code The code of the discount to fetch.
     * @param token The authorization token.
     * @return A Response object containing the discount.
     */
    @GET("discounts/code")
    suspend fun getDiscountByCode(
        @Query("code") code: String,  // Code of the discount to fetch
        @Header("Authorization") token: String?  // Authorization header with token
    ): Response<Discount>

    @PUT("orders")
    suspend fun updateOrderStatus(
        @Query("orderId") orderId: Long,
        @Query("statusId") statusId: Long,
        @Header("Authorization") token: String?
    ): Response<Boolean>
}

