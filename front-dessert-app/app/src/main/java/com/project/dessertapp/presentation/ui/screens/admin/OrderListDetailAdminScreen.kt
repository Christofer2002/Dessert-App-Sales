package com.project.dessertapp.presentation.ui.screens.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.project.dessertapp.R
import com.project.dessertapp.model.entities.OrderDetails
import com.project.dessertapp.model.entities.Product
import com.project.dessertapp.presentation.ui.layouts.MainLayout
import com.project.dessertapp.presentation.viewmodel.LoginViewModel
import com.project.dessertapp.presentation.viewmodel.OrderViewModel
import com.project.dessertapp.presentation.viewmodel.ProductViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun OrderListDetailAdminScreen(
    orderId: Long,
    orderViewModel: OrderViewModel,
    productViewModel: ProductViewModel,
    navController: NavController,
    loginViewModel: LoginViewModel? = null
) {

    val dateFormatter = SimpleDateFormat("EEE, MMM d, yyyy 'at' hh:mm a", Locale.getDefault())

    // Method to get the order details by order ID
    LaunchedEffect(orderId) {
        orderViewModel.getOrderDetailsByOrderId(orderId)
    }

    // Get the order details list from the view model
    val orderDetailsList by orderViewModel.orderDetailsList.collectAsState()

    val order = orderDetailsList.firstOrNull()?.order

    MainLayout(
        showBackButton = true,
        showSearchBar = false,
        showIAButton = false,
        title = "Order TCK${order?.id ?: "Not Found"}",
        height = 0.9f,
        showOptionsAdmin = false,
        navController = navController,
        isAdminHome = true,
        showOptionsBottomAdmin = true,
        loginViewModel = loginViewModel
    ) {
        // Añadimos el scroll vertical
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            order?.let {
                // Principal information of the order
                Text(
                    text = "Order TCK${it.id}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color(0xFFFFF8E1))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // clients's info
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Customer",
                                tint = androidx.compose.ui.graphics.Color(0xFF6D4C41)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${it.user?.firstName} ${it.user?.lastName}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email",
                                tint = androidx.compose.ui.graphics.Color(0xFF6D4C41)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "${it.user?.email}", fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Information about the order
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Order Date",
                                tint = androidx.compose.ui.graphics.Color(0xFF6D4C41)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Order Date: ${dateFormatter.format(it.orderDate)}", fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // state of the order
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Status",
                                tint = androidx.compose.ui.graphics.Color(0xFF6D4C41)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Status: ${it.status.label}", fontSize = 16.sp, color = androidx.compose.ui.graphics.Color(0xFFD84315))
                        }
                    }
                }

                // Show details of the discount applied
                it.discount?.let { discount ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color(0xFFFFF3E0))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Discount Applied",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = androidx.compose.ui.graphics.Color(0xFFD84315)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Code: ${discount.code}", fontSize = 16.sp)
                            Text(text = "Description: ${discount.description}", fontSize = 16.sp)
                            Text(text = "Discount: ${discount.discountPercentage}%", fontSize = 16.sp)
                            Text(
                                text = "Valid From: ${dateFormatter.format(discount.validFrom)} to ${dateFormatter.format(discount.validTo)}",
                                fontSize = 14.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color(0xFFFDF0E6))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = it.deliveryInstructions, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Total: ₡${it.totalPrice}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Will be delivered on ${dateFormatter.format(it.deliverDate)}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "${orderDetailsList.size} product(s)")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section for the order details
                Text(
                    text = "Order Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                orderDetailsList.forEach { orderDetail ->
                    ProductDetailCard(orderDetail = orderDetail, product = orderDetail.product)
                }
            } ?: run {
                Text(text = "Order not found")
            }
        }
    }
}

@Composable
fun ProductDetailCard(orderDetail: OrderDetails, product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color(0xFFE0E0E0))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.img),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Name of the product
            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Unitary price and amount
            Text(text = "₡${product.unitPrice} x ${orderDetail.amount}", fontWeight = FontWeight.Bold, fontSize = 16.sp)

            // Subtotal
            Text(text = "Subtotal: ₡${product.unitPrice * orderDetail.amount}", fontWeight = FontWeight.Bold, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(text = product.description, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))

        }
    }
}
