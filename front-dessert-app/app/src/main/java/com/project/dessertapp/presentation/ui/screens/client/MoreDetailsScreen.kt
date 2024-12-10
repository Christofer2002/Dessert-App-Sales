package com.project.dessertapp.presentation.ui.screens.client

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.project.dessertapp.R
import com.project.dessertapp.model.entities.Product


import com.project.dessertapp.presentation.ui.layouts.MainLayout
import com.project.dessertapp.presentation.ui.theme.DessertSalesAppTheme
import com.project.dessertapp.presentation.viewmodel.LoginViewModel
import com.project.dessertapp.presentation.viewmodel.OrderViewModel
import com.project.dessertapp.presentation.viewmodel.ProductViewModel
import com.project.dessertapp.utils.CurrencyUtils

@Composable
fun MoreDetailsScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    productID: Long,
    orderViewModel: OrderViewModel,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel? = null
) {
    LaunchedEffect(productID) {
        productViewModel.getProductById(productID)
    }

    val product by productViewModel.selectedProduct.collectAsState()
    var quantity by remember { mutableIntStateOf(1) }

    MainLayout(
        showBackButton = true,
        showSearchBar = false,
        showIAButton = false,
        title = "Would you like to order the ${product?.name} ?",
        height = 0.9f,
        showOptionsAdmin = false,
        navController = navController,
        loginViewModel = loginViewModel
    ) {
        LaunchedEffect(key1 = productID) {
            productViewModel.getProductById(productID)
        }

        Log.i("FME Log -> ", "MoreDetailsScreen - Loading MoreDetailsScreen with productID: $productID")
        product?.let { DetailsSection(it, orderViewModel, quantity, navController, onQuantityChange = { newQuantity -> quantity = newQuantity }) }
    }
}
@Composable
fun DetailsSection(
    product: Product,
    orderViewModel: OrderViewModel,
    quantity: Int,
    navController : NavController,
    onQuantityChange: (Int) -> Unit // Callback for quantity change
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        AsyncImage(
            model = product.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = product.flavour + " " + product.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8B4513)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = product.description,
            fontSize = 23.sp,
            color = Color(0xFF8B4513),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Total Price",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B4513)
            )

            Text(
                text = CurrencyUtils.formatCurrency(product.unitPrice * quantity),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B4513)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
            ) {
                Text(text = "-", color = Color.White, fontSize = 18.sp)
            }

            Text(
                text = quantity.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B4513)
            )

            Button(
                onClick = { onQuantityChange(quantity + 1) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
            ) {
                Text(text = "+", color = Color.White, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                orderViewModel.addProductToCart(product, quantity)
                showDialog = true
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513)),
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Make an order",
                color = Color.White,
                fontSize = 18.sp
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(product.name) },
                text = { Text("Your product ${product.name} has been placed successfully in your shopping cart!", fontSize = 18.sp) },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            navController.navigate("shoppingCart")
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}


/*@Preview(showBackground = true)
@Composable
fun MoreDetailsScreenPreview() {
    DessertSalesAppTheme {
        val navController = rememberNavController()
        MoreDetailsScreen(navController = navController,
            productId = 1L,
            productFlavour = "Chocolate",
            productDurationDays = 7,
            productName = "Chocolate Cake",
            productPrice = 2500.0,
            productImage = "img",
            productDescription = "Delicious chocolate cake with rich flavor, perfect for any occasion.")
    }
}*/