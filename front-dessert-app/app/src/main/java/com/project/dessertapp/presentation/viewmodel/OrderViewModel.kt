package com.project.dessertapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.dessertapp.model.entities.Discount
import com.project.dessertapp.model.entities.Order
import com.project.dessertapp.model.entities.OrderDetails
import com.project.dessertapp.model.entities.Product
import com.project.dessertapp.model.entities.Status
import com.project.dessertapp.model.repository.OrderRepository
import com.project.dessertapp.model.state.CartItem
import com.project.dessertapp.model.state.OrderWithDetails
import com.project.dessertapp.model.state.StatusDTO
import com.project.dessertapp.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository, // Repository for orders
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _orderList = MutableStateFlow<List<Order>>(emptyList())
    val orderList: StateFlow<List<Order>> get() = _orderList

    private val _selectedStatus = MutableStateFlow<String>("Waiting") // Store the selected status
    val selectedStatus: StateFlow<String> get() = _selectedStatus

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val _selectedOrder = MutableStateFlow<Order?>(null)
    val selectedOrder: StateFlow<Order?> get() = _selectedOrder

    private val _orderDetailsList = MutableStateFlow<List<OrderDetails>>(emptyList())
    val orderDetailsList: StateFlow<List<OrderDetails>> get() = _orderDetailsList

    private val _instructions = MutableStateFlow<String>("")
    val instructions: StateFlow<String> get() = _instructions

    private val _discount = MutableStateFlow<Discount?>(null)
    val discount: StateFlow<Discount?> get() = _discount

    //list to store the products the customer selected

    private val _productStoreList = MutableStateFlow<List<CartItem>>(emptyList())
    val productStoreList: StateFlow<List<CartItem>> get() = _productStoreList

    fun addOrder(order: Order, orderDetails: List<OrderDetails>) {
        viewModelScope.launch {
            val orderWithDetails = OrderWithDetails(
                order = order,
                orderDetails = orderDetails
            )

            val addedOrder = orderRepository.addOrderWithDetails(orderWithDetails, tokenManager.getToken())  // Add order with details
            addedOrder?.let {
                _orderList.value += it
            } ?: run {
                _errorMessage.value = "Failed to add order"
            }
        }
    }



    fun getAllOrders() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val orderList =
                    orderRepository.getAllOrders(tokenManager.getToken()) // Llama al repositorio para obtener las órdenes
                _orderList.value = orderList ?: emptyList()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load orders: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Filtered list based on the selected status
    val filteredOrderList: StateFlow<List<Order>> = combine(
        _selectedStatus,
        _orderList
    ) { selectedLabel, orders ->
        orders.filter { order ->
            val statusLabel =
                Status.fromId(order.status.id)?.label  // Get the label from the status ID
            statusLabel == selectedLabel  // Compare the label with the selected status
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Function to update the selected status
    fun setSelectedStatus(status: String) {
        viewModelScope.launch {
            _selectedStatus.value = status
        }
    }

    fun updateOrderStatus(orderId: Long, newStatus: String) {
        viewModelScope.launch {
            // Search for the status in the enum
            val statusEnum = Status.values().find { it.label == newStatus }
            if (statusEnum != null) {
                val newStatusDTO = StatusDTO(id = statusEnum.id, label = statusEnum.label)

                // Call the repository to update the status
                val isUpdated = orderRepository.updateOrderStatus(orderId, newStatusDTO.id, tokenManager.getToken())

                if (isUpdated) {
                    //update the status in the list
                    val updatedOrders = _orderList.value.map { order ->
                        if (order.id == orderId) {
                            order.copy(status = newStatusDTO)
                        } else {
                            order
                        }
                    }
                    _orderList.value = updatedOrders
                }
            }
        }
    }



    fun getOrderById(orderId: Long) {
        viewModelScope.launch {
            // get the order
            val order = orderRepository.getOrderById(orderId, tokenManager.getToken())
            _selectedOrder.value = order
        }
    }

    fun getOrderDetailsByOrderId(orderId: Long) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val orderDetails =
                    orderRepository.getOrderDetailsByOrderId(orderId, tokenManager.getToken()) // Call the repository to get the order details
                _orderDetailsList.value = orderDetails ?: emptyList()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load order details"
            } finally {
                _isLoading.value = false
            }
        }
    }

    //this function adds products to the current list
    fun addProductToCart(newProduct: Product, quantity: Int = 1) {
        val currentProductList = _productStoreList.value.toMutableList()

        // Search for the product in the list
        val existingItem = currentProductList.find { it.product.id == newProduct.id }

        if (existingItem != null) {
            // If it exists, update the quantity
            existingItem.quantity += quantity
        } else {
            // Else add a new item
            currentProductList.add(CartItem(newProduct, quantity))
        }

        _productStoreList.value = currentProductList
    }

    fun updateProductQuantity(cartItem: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            // Create a mutable list to update the quantity
            val currentProductList = _productStoreList.value.toMutableList()

            if (newQuantity <= 0) {
                currentProductList.remove(cartItem) // Delete the item from the list
            } else {
                val index = currentProductList.indexOf(cartItem)
                if (index != -1) {
                    // Update the quantity of the item
                    val updatedItem = currentProductList[index].copy(quantity = newQuantity)
                    currentProductList[index] = updatedItem // Reemplace the item in the list
                }
            }
            _productStoreList.value = currentProductList
        }
    }

    fun setInstructions(newInstructions: String) {
        _instructions.value = newInstructions // Update the instructions
    }

    fun getDiscountByCode(code: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val discount =
                    orderRepository.getDiscountByCode(code, tokenManager.getToken()) // Call the repository to get the discount
                _discount.value = discount ?: Discount(0, "", "", 0.0, Date(), Date())
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load discount"
            } finally {
                _isLoading.value = false
            }
        }
    }

        fun clear() {
            _productStoreList.value = emptyList()
            _instructions.value = ""
            _discount.value = null
        }
}