package com.project.dessertapp.presentation.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.dessertapp.model.entities.Category
import com.project.dessertapp.presentation.ui.layouts.MainLayout
import com.project.dessertapp.presentation.viewmodel.CategoryViewModel
import com.project.dessertapp.presentation.viewmodel.LoginViewModel
import com.project.dessertapp.presentation.viewmodel.ProductViewModel

@Composable
fun ManageProductScreen(
    productId: Long?,
    productViewModel: ProductViewModel,
    categoryViewModel: CategoryViewModel,
    navController: NavController,
    loginViewModel: LoginViewModel? = null
) {
    categoryViewModel.findAllCategories()

    val isEditing = productId != null
    val productState = productViewModel.selectedProduct.collectAsState().value
    val categories = categoryViewModel.categoryList.collectAsState().value

    // States for product fields
    var nameState by remember { mutableStateOf("") }
    var flavourState by remember { mutableStateOf("") }
    var durationState by remember { mutableStateOf("") }
    var priceState by remember { mutableStateOf("") }
    var descriptionState by remember { mutableStateOf("") }
    var imageState by remember { mutableStateOf("") }
    var categoryIdState by remember { mutableLongStateOf(0L) }

    // Effect to fetch the product if we are editing
    LaunchedEffect(productId) {
        if (isEditing && productId != null) {
            productViewModel.getProductById(productId)
        } else {
            // Clear the states if we are not editing (for adding a new product)
            nameState = ""
            flavourState = ""
            durationState = ""
            priceState = ""
            descriptionState = ""
            imageState = ""
            categoryIdState = 0L
        }
    }

    // Effect to update the states when the product changes (when editing)
    LaunchedEffect(productState) {
        if (isEditing && productState != null) {
            nameState = productState.name
            flavourState = productState.flavour
            durationState = productState.durationDays.toString()
            priceState = productState.unitPrice.toString()
            descriptionState = productState.description
            imageState = productState.image
            categoryIdState = productState.categoryId
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            nameState = ""
            flavourState = ""
            durationState = ""
            priceState = ""
            descriptionState = ""
            imageState = ""
            categoryIdState = 0L
        }
    }

    val scrollState = rememberScrollState()

    MainLayout(
        showBackButton = true,
        showSearchBar = false,
        showIAButton = false,
        title = if (isEditing) productState?.name ?: "Add New Product" else "Add New Product",
        height = 0.9f,
        showOptionsAdmin = false,
        navController = navController,
        showOptionsBottomAdmin = true,
        loginViewModel = loginViewModel
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {
                Text(
                    text = if (isEditing) productState?.name ?: "" else "Add New Product",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .height(480.dp)
                    .verticalScroll(scrollState)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Product Name
                ProductNameField(
                    nameState = nameState,
                    onNameChange = { newName -> nameState = newName }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Product Flavour
                ProductFlavourField(
                    flavourState = flavourState,
                    onFlavourChange = { newFlavour -> flavourState = newFlavour }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Duration Days
                ProductDurationField(
                    durationState = durationState,
                    onDurationChange = { newDuration -> durationState = newDuration }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Product Price
                ProductPriceField(
                    priceState = priceState,
                    onPriceChange = { newPrice ->
                        priceState = newPrice
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Product Description
                ProductDescriptionField(
                    descriptionState = descriptionState,
                    onDescriptionChange = { newDescription ->
                        descriptionState = newDescription // Actualiza el estado desde el nivel superior
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Category Dropdown
                ProductCategoryDropdown(
                    categories = categories,
                    categoryIdState = categoryIdState,
                    onCategorySelected = { newCategoryId ->
                        categoryIdState = newCategoryId // Actualiza el estado desde el nivel superior
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ManageProductButton(
                    isEditing = isEditing,
                    productId = productId ?: 0L,
                    nameState = nameState,
                    priceState = priceState,
                    descriptionState = descriptionState,
                    imageState = imageState,
                    flavourState = flavourState,
                    durationState = durationState,
                    categoryIdState = categoryIdState,
                    productViewModel = productViewModel,
                    navController = navController
                )

                // Show delete button only if we are editing
                if (isEditing) {
                    DeleteProductButton(
                        productId = productId ?: 0L,
                        productViewModel = productViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingProductSection() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Loading product or product not found.")
    }
}

@Composable
fun ProductNameField(nameState: String, onNameChange: (String) -> Unit) {
    OutlinedTextField(
        value = nameState,
        onValueChange = { newName ->
            onNameChange(newName)
        },
        label = { Text(text = "Product Name") },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(4.dp)
    )
}

@Composable
fun ProductFlavourField(flavourState: String, onFlavourChange: (String) -> Unit) {
    OutlinedTextField(
        value = flavourState,
        onValueChange = { newFlavour ->
            onFlavourChange(newFlavour)
        },
        label = { Text(text = "Flavour") },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(4.dp)
    )
}

@Composable
fun ProductDurationField(durationState: String, onDurationChange: (String) -> Unit) {
    OutlinedTextField(
        value = durationState,
        onValueChange = { newDuration ->
            onDurationChange(newDuration)
        },
        label = { Text(text = "Duration Days") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(4.dp)
    )
}

@Composable
fun ProductPriceField(priceState: String, onPriceChange: (String) -> Unit) {
    OutlinedTextField(
        value = priceState,
        onValueChange = { newPrice ->
            onPriceChange(newPrice)
        },
        label = { Text(text = "Product Price") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(4.dp)
    )
}

@Composable
fun ProductDescriptionField(descriptionState: String,
                            onDescriptionChange: (String) -> Unit) {
    OutlinedTextField(
        value = descriptionState,
        onValueChange = { newDescription ->
            onDescriptionChange(newDescription)
        },
        label = { Text(text = "Product Description") },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(4.dp)
    )
}

@Composable
fun ProductCategoryDropdown(
    categories: List<Category>,
    categoryIdState: Long,
    onCategorySelected: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val selectedCategory =
        categories.find { it.id == categoryIdState }?.name ?: "Select Category"

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedCategory,
            onValueChange = { /* No direct editing of the dropdown */ },
            label = { Text(text = "Category") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(4.dp),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    onClick = {
                        onCategorySelected(category.id) // Llama a la lambda para actualizar el estado
                        expanded = false
                    },
                    text = { Text(text = category.name) }
                )
            }
        }
    }
}

@Composable
fun ManageProductButton(
    isEditing: Boolean,
    productId: Long,
    nameState: String,
    priceState: String,
    descriptionState: String,
    imageState: String,
    flavourState: String,
    durationState: String,
    categoryIdState: Long,
    productViewModel: ProductViewModel,
    navController: NavController
) {
    var showDialog by remember { mutableStateOf(false) }

    Button(
        onClick = { showDialog = true },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xff009951)),
    ) {
        Text(text = if (isEditing) "Update Product" else "Add Product")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = if (isEditing) "Confirm Update" else "Confirm Add") },
            text = { Text(text = if (isEditing) "Are you sure you want to update this product?" else "Are you sure you want to add this product?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        if (isEditing) {
                            productViewModel.updateProduct(
                                productId = productId,
                                name = nameState,
                                price = priceState.toDoubleOrNull(),
                                description = descriptionState,
                                image = imageState,
                                flavour = flavourState,
                                durationDays = durationState.toIntOrNull() ?: 0,
                                categoryId = categoryIdState
                            )
                        } else {
                            productViewModel.addProduct(
                                name = nameState,
                                price = priceState.toDoubleOrNull() ?: 0.0,
                                description = descriptionState,
                                image = imageState,
                                flavour = flavourState,
                                durationDays = durationState.toIntOrNull() ?: 0,
                                categoryId = categoryIdState
                            )
                        }

                        navController.popBackStack()
                    }
                ) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(text = "No")
                }
            }
        )
    }
}

@Composable
fun DeleteProductButton(
    productId: Long,
    productViewModel: ProductViewModel,
    navController: NavController
) {
    var showDialog by remember { mutableStateOf(false) }

    Button(
        onClick = { showDialog = true },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
    ) {
        Text(text = "Delete Product")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm Deletion") },
            text = { Text(text = "Are you sure you want to delete this product?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        productViewModel.deleteProduct(productId)
                        navController.popBackStack() // Go back after deleting
                    }
                ) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text(text = "No")
                }
            }
        )
    }
}