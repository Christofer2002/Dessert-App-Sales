package com.project.dessertapp.presentation.ui.screens.admin

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.project.dessertapp.R
import com.project.dessertapp.model.entities.Product
import com.project.dessertapp.navigation.NavRoutes
import com.project.dessertapp.presentation.ui.layouts.MainLayout
import com.project.dessertapp.presentation.ui.theme.ColorBanners
import com.project.dessertapp.presentation.viewmodel.LoginViewModel
import com.project.dessertapp.presentation.viewmodel.ProductViewModel

@Composable
fun ProductAdminScreen(
    productViewModel: ProductViewModel,
    modifier: Modifier = Modifier,
    navController: NavController,
    loginViewModel: LoginViewModel? = null
) {
    LaunchedEffect(true) {
        productViewModel.findAllProducts()
    }

    val productList by productViewModel.productList.collectAsState()
    val isLoading by productViewModel.isLoading.collectAsState()
    val errorMessage by productViewModel.errorMessage.collectAsState()

    if (isLoading) {
        // Show loading message
        Text(text = "Loading products...")
    } else if (errorMessage != null) {
        // Show error message
        Text(text = errorMessage ?: "Unknown error")
    } else {

        MainLayout(
            showBackButton = true,
            showSearchBar = true,
            showIAButton = false,
            title = "Products Admin Center",
            height = 0.9f,
            showOptionsAdmin = false,
            navController = navController,
            showOptionsBottomAdmin = true,
            loginViewModel = loginViewModel
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp)
                ) {
                    items(productList) { product ->
                        ProductItem(product, navController)
                    }
                }

                AddNewProductButton(
                    onClick = {
                        // Navigate to ManageProductScreen with no productId
                        navController.navigate(NavRoutes.ManageProductAdmin.route.replace("{productId}", "0"))
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun AddNewProductButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onClick() }
            .shadow(4.dp, RoundedCornerShape(10.dp))
            .background(Color(0xff009951), RoundedCornerShape(10.dp))
            .size(210.dp, 40.dp)
            .padding(horizontal = 8.dp, vertical = 3.dp),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .wrapContentHeight()
                .fillMaxWidth(),
            text = "Add a new product",
            color = Color(0xffffffff),
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun ProductItem(product: Product, navController: NavController? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White // Set your desired background color here
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .shadow(4.dp, shape = RoundedCornerShape(8.dp))

        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = product.image,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.img),
                    error = painterResource(id = R.drawable.img),
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(
                    modifier = Modifier.weight(1f)
                )
                {
                    Column {
                        Text(text = product.name)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(    //verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "₡${product.unitPrice}")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            navController?.let {
                                try {
                                    navController.navigate(NavRoutes.ProductEditAdmin.route.replace("{productId}", product.id.toString()))
                                } catch (e: Exception) {
                                    Log.e("FME - Execution error", " -> ", e)
                                }
                            }
                        },
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .sizeIn(maxWidth = 160.dp, maxHeight = 35.dp),
                        colors = ButtonDefaults.buttonColors(ColorBanners)
                    ) {
                        Text(text = "Manage Product")
                    }
                }
            }
        }
    }
}