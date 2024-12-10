package com.project.dessertapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.dessertapp.model.datasources.ProductProvider
import com.project.dessertapp.model.entities.Product
import com.project.dessertapp.model.repository.ProductRepository
import com.project.dessertapp.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ProductViewModel handles fetching and providing product data to the UI.
 */
@HiltViewModel
class ProductViewModel @Inject constructor (
    private val productRepository: ProductRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val productList: StateFlow<List<Product>> get() = _productList

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> get() = _selectedProduct

    /**
     * Fetch all products and update the state.
     */
    fun findAllProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val productList = productRepository.getAllProducts(tokenManager.getToken())
                _productList.value = productList ?: emptyList()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load products"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun findProductsByCategoryId(categoryId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val productList = productRepository.getProductsByCategoryId(categoryId, tokenManager.getToken())
                _productList.value = productList ?: emptyList()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load products"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun findProductsByEventId(eventId: Long?) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val productList = productRepository.getProductsByEventId(eventId, tokenManager.getToken())
                _productList.value = productList ?: emptyList()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load products"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Add a new product to the product list using the repository.
     */
    fun addProduct(
        name: String,
        price: Double,
        description: String,
        image: String,
        flavour: String,
        durationDays: Int,
        categoryId: Long
    ) {
        viewModelScope.launch {
            try {
                val newProduct = Product(
                    name = name,
                    unitPrice = price,
                    description = description,
                    image = image,
                    flavour = flavour,
                    durationDays = durationDays,
                    categoryId = categoryId
                )
                val addedProduct = productRepository.addProduct(newProduct, tokenManager.getToken())

                addedProduct?.let {
                    _productList.value += it  // Add to the state if the product is added successfully
                } ?: run {
                    _errorMessage.value = "Failed to add product"
                }
            } catch (e: Exception) {
                // Set the error message with exception details
                _errorMessage.value = "Exception occurred: ${e.message}"
            }
        }
    }


    /**
     * Fetch a product by its ID and set it as the selected product.
     */
    fun getProductById(productId: Long) {
        viewModelScope.launch {
            try {
                val product = productRepository.getProductById(productId, tokenManager.getToken())
                _selectedProduct.value = product
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load product"
            }
        }
    }

    /**
     * Update a product and modify the state.
     */
    fun updateProduct(
        productId: Long,
        name: String? = null,
        price: Double? = null,
        description: String? = null,
        image: String? = null,
        flavour: String? = null,
        durationDays: Int? = null,
        categoryId: Long? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val updatedProduct = productRepository.updateProduct(
                    tokenManager.getToken(),
                    productId = productId,
                    name = name,
                    price = price,
                    description = description,
                    image = image,
                    flavour = flavour,
                    durationDays = durationDays,
                    categoryId = categoryId
                )

                if (updatedProduct != null) {
                    // Update the product list and selected product
                    _productList.value = _productList.value.map { product ->
                        if (product.id == productId) updatedProduct else product
                    }
                    _selectedProduct.value = updatedProduct
                } else {
                    _errorMessage.value = "Error updating product"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update product: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Delete a product by its ID.

     * Delete a product by its ID using the repository.

     */
    fun deleteProduct(productId: Long) {
        viewModelScope.launch {
            try {
                val isDeleted = productRepository.deleteProductById(productId, tokenManager.getToken())
                if (isDeleted) {
                    _productList.value = _productList.value.filter { it.id != productId }
                    _selectedProduct.value = null
                } else {
                    _errorMessage.value = "Failed to delete product"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error while deleting product"
            }
        }
    }
}
