package com.project.dessertapp.presentation.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project.dessertapp.model.entities.Order
import com.project.dessertapp.model.state.TabSelectionState
import com.project.dessertapp.presentation.ui.layouts.MainLayout
import com.project.dessertapp.presentation.viewmodel.LoginViewModel
import com.project.dessertapp.presentation.viewmodel.OrderViewModel

@Composable
fun OrderListAdminScreen(
    orderViewModel: OrderViewModel,
    navController: NavController,
    loginViewModel: LoginViewModel? = null
) {

    var isFirstLoad by remember { mutableStateOf(true) }

    LaunchedEffect(isFirstLoad) {
        if (isFirstLoad) {
            orderViewModel.setSelectedStatus("Waiting")
            orderViewModel.getAllOrders()
            isFirstLoad = false  // change the value to false to avoid calling the function again
        }
    }

    // observe the order list
    val orderList by orderViewModel.filteredOrderList.collectAsState(initial = emptyList())

    var tabSelectionState by remember { mutableStateOf(TabSelectionState(isWaitingSelected = true)) }

    // call the function to get all orders
    fun handleOptionSelected(option: String = "Waiting") {
        tabSelectionState = when (option) {
            "Waiting" -> TabSelectionState(isWaitingSelected = true)
            "Accepted" -> TabSelectionState(isAcceptedSelected = true)
            "Rejected" -> TabSelectionState(isRejectedSelected = true)
            else -> TabSelectionState(isWaitingSelected = true)
        }
        orderViewModel.setSelectedStatus(option)
    }

    MainLayout(
        showBackButton = true,
        showSearchBar = false,
        showIAButton = false,
        title = "Orders",
        height = 0.9f,
        showOptionsAdmin = true,
        navController = navController,
        isAdminHome = false,
        onOptionSelected = { handleOptionSelected(it) },
        tabSelectionState = tabSelectionState,
        showOptionsBottomAdmin = true,
        loginViewModel = loginViewModel
    ) {
        OrderList(
            navController = navController,
            orders = orderList,
            tabSelectionState = tabSelectionState,
            onAccept = { orderId -> orderViewModel.updateOrderStatus(orderId, "Accepted") },
            onReject = { orderId -> orderViewModel.updateOrderStatus(orderId, "Rejected") }
        )
    }
}

@Composable
fun OrderList(navController : NavController, orders: List<Order>, tabSelectionState: TabSelectionState, onAccept: (Long) -> Unit, onReject: (Long) -> Unit) {
    // Renderiza la lista de órdenes
    Column {
        orders.forEach { order ->
            OrderItem(navController = navController, order, tabSelectionState = tabSelectionState, onAccept = onAccept, onReject = onReject)  // for each order, render an OrderItem
        }
    }
}

@Composable
fun OrderItem(navController : NavController,order: Order, tabSelectionState: TabSelectionState, onAccept: (Long) -> Unit, onReject: (Long) -> Unit) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
    ) {
        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .padding(start = 0.dp, end = 0.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .padding(start = 0.dp, end = 0.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .wrapContentSize()
                            .clipToBounds(),
                    ) {
                        Box(
                            contentAlignment = Alignment.TopStart,
                            modifier = Modifier
                                .background(Color(0xffeaddff), RoundedCornerShape(100.dp))
                                .size(40.dp, 40.dp)
                                .clip(RoundedCornerShape(100.dp)),
                        ) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = order.deliveryInstructions.first().toString(),
                                color = Color(0xff21005d),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(start = 8.dp)
                            .clipToBounds(),
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Start),
                            text = "TCK${order.id}",
                            color = Color(0xff693712),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Left,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.wrapContentSize()
                    ) {
                        IconButton(
                            onClick = { navController.navigate("order-list-detail-admin/${order.id}") }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = "View Order",
                                tint = Color(0xffF9A825),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        if (!tabSelectionState.isAcceptedSelected) {
                            IconButton(
                                onClick = { order.id?.let { onAccept(it) } }  // Cambia el estado a "Accepted"
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Accept Order",
                                    tint = Color(0xff388E3C),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        if (!tabSelectionState.isRejectedSelected) {
                            IconButton(
                                onClick = { order.id?.let { onReject(it) } }  // Cambia el estado a "Rejected"
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Reject Order",
                                    tint = Color(0xffD32F2F),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}