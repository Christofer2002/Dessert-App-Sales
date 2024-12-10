package com.project.dessertapp.presentation.ui.screens.client

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.project.dessertapp.R
import com.project.dessertapp.model.entities.Product
import com.project.dessertapp.presentation.ui.layouts.MainLayout
import com.project.dessertapp.presentation.viewmodel.LoginViewModel
import com.project.dessertapp.presentation.viewmodel.OrderViewModel
import com.project.dessertapp.presentation.viewmodel.ProductViewModel
import com.project.dessertapp.utils.CurrencyUtils
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale


@Composable
fun CakeScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    orderViewModel: OrderViewModel,
    productID: Long,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel? = null
) {
    LaunchedEffect(productID) {
        productViewModel.getProductById(productID)
    }

    val product by productViewModel.selectedProduct.collectAsState()

    MainLayout(
        showBackButton = true,
        showSearchBar = false,
        showIAButton = false,
        title = "Would you like to order the ${product?.name}?",
        height = 0.9f,
        showOptionsAdmin = false,
        navController = navController,
        loginViewModel = loginViewModel
    ) {
        Log.i("FME Log -> ", "CakeScreen - Loading CakeScreen with productID: $productID")
        product?.let { NewDetailsSection(it, orderViewModel) }
    }
}

@Composable
fun NewDetailsSection(product: Product, orderViewModel: OrderViewModel) { // Agregar OrderViewModel como parámetro
    var showDialog by remember { mutableStateOf(false) }
    var quantity by remember { mutableIntStateOf(1) } // State for the quantity

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = product.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.img),
            error = painterResource(id = R.drawable.img),
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = product.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8B4513)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = CurrencyUtils.formatCurrency(product.unitPrice * quantity),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8B4513)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { if (quantity > 1) quantity -= 1 }, // Decrement quantity
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513)),
                shape = CircleShape,
                modifier = Modifier
            ) {
                Text(text = "-", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = quantity.toString(), // Muestra la cantidad actual
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B4513)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { quantity += 1 }, // Incrementar cantidad
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513)),
                shape = CircleShape,
                modifier = Modifier
            ) {
                Text(text = "+", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.weight(1f)) // Espacio flexible para empujar el siguiente botón a la derecha

            Button(
                onClick = {
                    orderViewModel.addProductToCart(product, quantity) // Add product to cart
                    showDialog = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513)),
                shape = RoundedCornerShape(40.dp),
                modifier = Modifier
                    .height(50.dp)
            ) {
                Text(
                    text = "Make an order",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(product.name) },
            text = { Text("Your product ${product.name} has been placed successfully in your shopping cart!", fontSize = 18.sp) },
            confirmButton = {
                Button(
                    onClick = { showDialog = false } // Cierra el diálogo al confirmar
                ) {
                    Text("OK")
                }
            }
        )
    }
}


/*@Preview(showBackground = true)
@Composable
fun CakeScreenPreview() {
    val product = Product(
        id = 1L,
        name = "Chocolate Bar",
        flavour = "Dark Chocolate",
        durationDays = 365,
        unitPrice = 2.99,
        description = "Rich and smooth dark chocolate bar.",
        image = "https://example.com/images/chocolate_bar.jpg",
        categoryId = 101L
    )
    DessertSalesAppTheme {
        val navController = rememberNavController()
        val productID: Long = 1
        val productService: ProductService = ProductService()
        val productRepository: ProductRepository = ProductRepository(productService)
        val productViewModel: ProductViewModel = ProductViewModel(productRepository)
        val product: Product? = productViewModel.getProductByIdSync(productID)

        if (product != null) {
            NewDetailsSection(product.image, product.name, product.unitPrice)
        }
        CakeScreen(  navController = navController, productViewModel = productViewModel,1)
    }
}*/