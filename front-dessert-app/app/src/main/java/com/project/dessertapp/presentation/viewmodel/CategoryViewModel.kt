package com.project.dessertapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.dessertapp.model.entities.Category
import com.project.dessertapp.model.repository.CategoryRepository
import com.project.dessertapp.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * CategoryViewModel handles fetching and providing product data to the UI.
 */
@HiltViewModel
class CategoryViewModel  @Inject constructor (
    private val categoryRepository: CategoryRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    // MutableStateFlow that holds the list of categories
    private val _categoryList = MutableStateFlow<List<Category>>(emptyList())
    val categoryList: StateFlow<List<Category>> get() = _categoryList

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> get() = _selectedCategory

    // Function to find and set all products
    fun findAllCategories() {
        viewModelScope.launch {
            val categoryList = categoryRepository.getAllCategories(tokenManager.getToken())
            _categoryList.value = categoryList ?: emptyList()
        }
    }

    /**
     * Find a category by its ID and set it as the selected category.
     */
    fun getCategoryById(productId: Long) {
        viewModelScope.launch {
            val product = categoryRepository.getCategoryById(productId)
            _selectedCategory.value = product
        }
    }

}