package com.project.dessertapp.presentation.ui.screens.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.project.dessertapp.R
import com.project.dessertapp.model.entities.Discount
import com.project.dessertapp.model.entities.LoginResponse
import com.project.dessertapp.model.entities.Order
import com.project.dessertapp.model.entities.OrderDetails
import com.project.dessertapp.model.entities.Status
import com.project.dessertapp.model.entities.User
import com.project.dessertapp.model.state.CartItem
import com.project.dessertapp.model.state.StatusDTO
import com.project.dessertapp.presentation.ui.layouts.MainLayout
import com.project.dessertapp.presentation.viewmodel.LoginViewModel
import com.project.dessertapp.presentation.viewmodel.OrderViewModel
import com.project.dessertapp.utils.ApplicationInsightsLogger
import com.project.dessertapp.utils.CurrencyUtils
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun OrderListScreen(
    orderViewModel: OrderViewModel,
    modifier: Modifier = Modifier,
    navController: NavController,
    loginViewModel: LoginViewModel? = null
) {

    val productListStore by orderViewModel.productStoreList.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    // Verify if the list is empty
    if (productListStore.isEmpty()) {
        showDialog = true // Show the dialog
    }

    // Show the dialog if the list is empty
    if (!showDialog) {
        MainLayout(
            showBackButton = true,
            showSearchBar = false,
            showIAButton = false,
            title = "This is your order list, please check it!",
            height = 0.9f,
            showOptionsAdmin = false,
            navController = navController,
            loginViewModel = loginViewModel
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .verticalScroll(rememberScrollState()) // Enable vertical scrolling
            ) {
                OrderSection(orderViewModel, productListStore, navController, loginViewModel = loginViewModel!!, onDismissDialogNoProduct = { showDialog = false })
            }
        }
    }

    if (showDialog) {
        MainLayout(
            showBackButton = true,
            showSearchBar = false,
            showIAButton = false,
            title = "No products found",
            height = 0.9f,
            showOptionsAdmin = false,
            navController = navController
        )
        AlertDialog(
            onDismissRequest = { /* Evitar que el diálogo se cierre sin interacción */ },
            title = { Text("No products found") },
            text = { Text("There are no products in your order list. Please add products before proceeding.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        orderViewModel.clear()
                        navController.navigate("home") // Navigate to the home screen
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun OrderSection(orderViewModel: OrderViewModel, productListStore: List<CartItem>, navController: NavController, loginViewModel: LoginViewModel? = null, onDismissDialogNoProduct: () -> Unit) {
    // Get the total price of the order
    var totalPrice by remember { mutableDoubleStateOf(0.0) }
    totalPrice = productListStore.sumOf { it.product.unitPrice * it.quantity } // Calculate the total price
    val discount by orderViewModel.discount.collectAsState()
    val customer = loginViewModel?.userResponse?.collectAsState()?.value

    // Calculate formatted total price based on the current total price
    val formattedTotalPrice = String.format("%.2f", totalPrice).toDouble()

    var newTotal = formattedTotalPrice // Use the new total after discount

    if (discount != null) {
        // Update the total price when the discount is applied
        newTotal *= (1 - (discount?.discountPercentage ?: 0.0) / 100)
    }

    // Calculate the total preparation days
    val uniqueProducts = productListStore.map { it.product }.distinctBy { it.id }
    val totalPreparationDays = uniqueProducts.sumOf { it.durationDays }

    // Calculate the delivery date based on total preparation days
    val currentDate = Date()
    val calendar = Calendar.getInstance()
    calendar.time = currentDate
    calendar.add(Calendar.DAY_OF_YEAR, totalPreparationDays)
    val deliveryDate = calendar.time

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Text(
            text = "Your Shopping Cart",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(25.dp))

        HorizontalDivider(thickness = 1.dp, color = Color.Black)
        InstructionsSection(orderViewModel)
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(thickness = 1.dp, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))

        DiscountSection(orderViewModel, discount, newTotal) { newDiscountedTotal ->
            totalPrice = newDiscountedTotal // Update totalPrice when the discount is applied
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(thickness = 1.dp, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))

        if (productListStore.isNotEmpty()) {
            ProductInformationSection(productListStore) { cartItem, newQuantity ->
                // Call the view model to update the quantity
                orderViewModel.updateProductQuantity(cartItem, newQuantity)
                totalPrice = productListStore.sumOf { it.product.unitPrice * it.quantity } // Update the total price
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(thickness = 1.dp, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))

        if (productListStore.isNotEmpty()) {
            val subTotal = productListStore.sumOf { it.product.unitPrice * it.quantity }
            customer?.let {
                OrderInformationSection(
                    it.firstName + " " + it.lastName,
                    it.username,
                    currentDate,
                    deliveryDate,
                    "Will be in process",
                    productListStore.size,
                    subTotal,
                    discount?.discountPercentage ?: 0.0
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        // Use the new total after discount
        TotalOrderPriceSection(
            productListStore = productListStore,
            discount = discount,
            orderViewModel = orderViewModel,
            totalPrice = newTotal, // Total price after discount
            navController = navController,
            customer = customer,
            onDismissDialogNoProduct = onDismissDialogNoProduct
        )
    }
}

@Composable
fun InstructionsSection(orderViewModel: OrderViewModel) {
    var showDialog by remember { mutableStateOf(false) } // Control the dialog visibility
    var instructionText by remember { mutableStateOf("") } // Save the instruction text
    var showSnackbar by remember { mutableStateOf(false) }

    val instructions by orderViewModel.instructions.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Info, contentDescription = "Instructions Icon")
            Text("Instructions", fontWeight = FontWeight.Bold, color = Color(0xFF583D14))
        }
        Spacer(modifier = Modifier.padding(20.dp))
        Column {
            Text("Leave at door, please!", fontSize = 15.sp, color = Color(0xFF583D14))
            Spacer(modifier = Modifier.padding(2.dp))

            Button(
                onClick = { showDialog = true }, // Show the dialog
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color(0xBAECB86B)),
                modifier = Modifier
                    .height(35.dp)
                    .width(220.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Add Instructions Icon",
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                        tint = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Add instructions for delivery", fontSize = 11.sp, color = Color.Black)
                }
            }

            if (instructions.isNotBlank()) {
                // Show the instructions if they exist
                Text(
                    text = "Instructions: $instructions",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF583D14)
                )
            }

            // Dialog to add delivery instructions
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Add Delivery Instructions") },
                    text = {
                        Column {
                            TextField(
                                value = instructionText,
                                onValueChange = { instructionText = it },
                                label = { Text("Instructions") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                orderViewModel.setInstructions(instructionText)
                                showSnackbar = true
                                showDialog = false // Close the dialog
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showDialog = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
    }

    // Snackbar for feedback
    if (showSnackbar) {
        Snackbar(
            action = {
                Button(onClick = { showSnackbar = false }) {
                    Text("Dismiss")
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Instructions saved successfully!")
        }
    }
}

//Icon(Icons.Default.CardGiftcard, contentDescription = "Discount Icon")
@Composable
fun DiscountSection(orderViewModel: OrderViewModel, discount: Discount?, newTotal: Double, onTotalPriceChange: (Double) -> Unit) {
    var showDialog by remember { mutableStateOf(false) } // Control the visibility of the dialog
    var discountCodeText by remember { mutableStateOf("") } // Save the discount code
    val errorMessage by orderViewModel.errorMessage.collectAsState() // Capture error messages
    var showSnackbar by remember { mutableStateOf(false) }

    val discountCode = discount?.code ?: "" // Usar una cadena vacía si discount es null

    discountCodeText = discountCode // Set the discount code from the ViewModel

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Icon(Icons.Default.CardGiftcard, contentDescription = "Discount Icon")
            Text("Discount", fontWeight = FontWeight.Bold, color = Color(0xFF583D14))
        }
        Spacer(modifier = Modifier.padding(20.dp))
        Column (
            modifier = Modifier .padding(start = 16.dp)
        ) {
            Text("Enter the discount code", fontSize = 15.sp, color = Color(0xFF583D14))
            Spacer(modifier = Modifier.padding(4.dp))

            Button(
                onClick = { showDialog = true }, // Show the dialog on click
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color(0xBAECB86B)),
                modifier = Modifier.height(35.dp).width(220.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = "Favorite Icon",
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                        tint = Color(0xFF703F15)
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Redeem for discount", fontSize = 12.sp, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))

            val formatterNewPrice = String.format("%.2f", newTotal).toDouble()

            if ((discount?.discountPercentage ?: 0.0) > 0.0) {
                // Call the onTotalPriceChange to update the total price in OrderSection
                onTotalPriceChange(formatterNewPrice)
                // Show the total after discount
                Text(
                    "Total after applying discount: ₡${formatterNewPrice}",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF583D14)
                )
            }

            // Dialog to add discount code
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Add Discount") },
                    text = {
                        Column {
                            TextField(
                                value = discountCodeText,
                                onValueChange = { discountCodeText = it },
                                label = { Text("Discount code") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                orderViewModel.getDiscountByCode(discountCodeText) // CHANGE FOR CODE TEXT
                                showSnackbar = true
                                showDialog = false // Close the dialog
                            }
                        ) {
                            Text("Apply")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // Display error message if discount not found
            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
    }

    // Snackbar for feedback
    if (showSnackbar) {
        Snackbar(
            action = {
                Button(onClick = { showSnackbar = false }) {
                    Text("Dismiss")
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Discount applied successfully!")
        }
    }
}

@Composable
fun ProductInformationSection(productStore: List<CartItem>, onQuantityChange: (CartItem, Int) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        for (cartItem in productStore) {
            val product = cartItem.product
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AsyncImage(
                    model = product.image,
                    contentDescription = "Product Image",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.img),
                    error = painterResource(id = R.drawable.img),
                    modifier = Modifier.size(60.dp)
                )
                Column(
                    modifier = Modifier.weight(1f) .padding(start = 14.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(product.name, fontWeight = FontWeight.Bold, color = Color(0xFF583D14))
                    Text(CurrencyUtils.formatCurrency(product.unitPrice * cartItem.quantity), fontWeight = FontWeight.SemiBold)
                    Text("${cartItem.quantity} product(s)")
                    Text("Duration: ${product.durationDays} day(s)", color = Color.Gray)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            val newQuantity = cartItem.quantity - 1
                            onQuantityChange(cartItem, newQuantity)
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Remove,
                            contentDescription = "Decrease quantity",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            val newQuantity = cartItem.quantity + 1
                            onQuantityChange(cartItem, newQuantity)
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Increase quantity",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Black
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@Composable
fun OrderInformationSection(//3 entidades por parametro
    customerName: String,
    customerEmail: String,
    orderDate: Date,
    deliveryDate: Date,
    status: String,
    totalProducts: Int,
    subTotal: Double,
    discountPercentage: Double
) {
    /*val formatter = SimpleDateFormat("yyyy-MM-dd")
    val dateString = "2024-09-28"
    val date: Date = formatter.parse(dateString)*/
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedOrderDate = formatter.format(orderDate)
        val formattedDeliveryDate = formatter.format(deliveryDate)

        Text(
            text = "Order Information",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center, // Cambiado a TextAlign
            fontSize = 20.sp,
            modifier = Modifier.fillMaxWidth() // Opcional: para que ocupe el ancho completo
        )
        Spacer(modifier = Modifier.height(25.dp))
        InfoRow("Customer name", customerName)
        InfoRow("Customer email", customerEmail)
        InfoRow("Order date", formattedOrderDate)
        InfoRow("Delivery date", formattedDeliveryDate)
        InfoRow("Status", status)
        InfoRow("Total products", totalProducts.toString())
        InfoRow("Sub Total", "₡ $subTotal")
        // Show discount information if applicable
        if (discountPercentage > 0.0) {
            InfoRow("Discount", "${discountPercentage}%")
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.SemiBold)
        Text(value)
    }
}

@Composable
fun TotalOrderPriceSection(
    productListStore: List<CartItem>,  // Lista de productos en el carrito
    discount: Discount?,               // El descuento aplicado
    orderViewModel: OrderViewModel,    // ViewModel para manejar la lógica
    totalPrice: Double,                // Precio total calculado
    navController: NavController,       // NavController para la navegación
    customer: LoginResponse?,
    onDismissDialogNoProduct: () -> Unit
) {
    val instructions by orderViewModel.instructions.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(Order(id = 0, orderDate = Date(), deliverDate = Date(), totalPrice = 0.0, deliveryInstructions = "", user = null, status = StatusDTO(1, "Waiting"), discount = null)) }

    val displayInstructions = instructions.ifBlank {
        "No instructions provided"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Total Order Price", fontWeight = FontWeight.Bold)
        Text(CurrencyUtils.formatCurrency(totalPrice), fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Button(
            onClick = {
                // Call the function to confirm the order
                success = confirmOrder(
                    productListStore = productListStore,
                    discount = discount,
                    customer = customer,
                    instructions = displayInstructions,
                    totalPrice = totalPrice,
                    orderViewModel = orderViewModel
                )
                    showDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF8F6234))
        ) {
            Text("Confirm order", color = Color.White)
        }
    }

    // Show dialog when the order is successful
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Order Successful") },
            text = { Text("Your order has been placed successfully!", fontSize = 18.sp) },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onDismissDialogNoProduct()
                        navController.navigate("home") // Navigate to the home screen
                        orderViewModel.clear() // Clear the order list
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

fun confirmOrder(
    productListStore: List<CartItem>,
    discount: Discount?,
    customer: LoginResponse?,
    instructions: String,
    totalPrice: Double,
    orderViewModel: OrderViewModel
) : Order {

    val user = User(
        id = customer?.id ?: 0,
        createdDate = Date(),
        email = customer?.username ?: "",
        enabled = true,
        firstName = customer?.firstName ?: "",
        lastName = customer?.lastName ?: "",
    )

    val statusDTO = StatusDTO(1, "Waiting")
    val order = Order(
        orderDate = Date(), // Current date
        deliverDate = Date(), // the delivery is immediate
        totalPrice = totalPrice, // The total after applying discounts
        deliveryInstructions = instructions, // Delivery instructions
        user = user, // The user's ID passed as a parameter
        status = statusDTO, // Initial status
        discount = discount // Apply discount if it exists
    )

    val orderDetailsList = productListStore.map { cartItem ->
        OrderDetails(
            amount = cartItem.quantity,
            order = null, // Reference to the order
            product = cartItem.product // Id of the product
        )
    }
    orderViewModel.addOrder(order, orderDetailsList)
    try {
        val appIn = ApplicationInsightsLogger()
        appIn.logEvent("LOG-DessertSalesApp-Frontend: New order placed by user -> ${user.id}")
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return order
}

