package com.project.dessertapp.presentation.ui.screens.client

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.dessertapp.R
import com.project.dessertapp.model.datasources.ProductProvider
import com.project.dessertapp.model.entities.Product
import com.project.dessertapp.presentation.ui.layouts.MainLayout
import com.project.dessertapp.presentation.ui.theme.ColorBanners
import com.project.dessertapp.presentation.viewmodel.ProductViewModel
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.project.dessertapp.navigation.NavRoutes
import com.project.dessertapp.presentation.viewmodel.LoginViewModel
import com.project.dessertapp.presentation.viewmodel.OrderViewModel

@Composable
fun ProductDetailScreen(
    categoryId: Long?,
    eventId: Long?,
    productViewModel: ProductViewModel,
    modifier: Modifier = Modifier,
    navController: NavController,
    loginViewModel: LoginViewModel? = null
) {

    if(categoryId != null){
        LaunchedEffect(categoryId) {
            productViewModel.findProductsByCategoryId(categoryId)
        }
    }else{
        LaunchedEffect(eventId) {
            productViewModel.findProductsByEventId(eventId)
        }
    }


    val isLoading by productViewModel.isLoading.collectAsState()
    val errorMessage by productViewModel.errorMessage.collectAsState()

    if (isLoading) {
        // Show loading message
        Text(text = "Loading products...")
    } else if (errorMessage != null) {
        // Show error message
        Text(text = errorMessage ?: "Unknown error")
    } else {
        val productList by productViewModel.productList.collectAsState()

        MainLayout (
            showBackButton = true,
            showSearchBar = true,
            showIAButton = false,
            title = "What would you like to order?",
            height = 0.9f,
            showOptionsAdmin = false,
            navController = navController,
            loginViewModel = loginViewModel
        ){

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(2.dp)
        ) {

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(productList) { product ->
                    ProductItem(product = product, navController = navController)
                }
                }
            }
            //Spacer(modifier = Modifier.height(8.dp))
        }
    }
    }



@Composable
fun ProductItem(product: Product, navController: NavController) {
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
                        Text(
                            text = product.name,
                            fontSize = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(    //verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "₡${product.unitPrice}",
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            navController.navigate( "more-detail-screen/${product.id}")
                            Log.i("FME Log -> ","ProductDetailScreen - Trying to load MoreDetailsScreen with productID: "+ product.id+"\n")
                            if (product.name.isNotBlank() && product.unitPrice > 0 && product.description.isNotBlank() && product.flavour.isNotBlank()) {

                            } else {
                                Log.e("ProductItem", "Invalid product details")
                            }
                                  },  // Navegar a MoreDetailsScreen con el productId
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .sizeIn(maxWidth = 128.dp, maxHeight = 35.dp),
                        colors = ButtonDefaults.buttonColors(ColorBanners)) {
                        Text(text = "More Details", fontSize = 13.sp)
                    }
                }
                Column (){
                    IconButton(onClick = {
                        navController.navigate("productCake/${product.id}")
                        Log.i("FME Log -> ","ProductDetailScreen - Trying to load CakeScreen with productID: "+ product.id+"\n")

                        if (product.name.isNotBlank() && product.unitPrice > 0) {

                        } else {
                            Log.e("ProductItem", "Invalid product details")
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.AddBox,
                            contentDescription = "Favorite Icon",
                            tint = Color(0xffa27146),
                            modifier = Modifier
                                .size(45.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            }
            }
        }
    }
