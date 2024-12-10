package com.project.dessertapp.presentation.ui.screens.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.dessertapp.R
import com.project.dessertapp.presentation.ui.layouts.MainLayout
import com.project.dessertapp.presentation.ui.theme.DessertSalesAppTheme
import com.project.dessertapp.presentation.viewmodel.LoginViewModel

@Composable
fun HomeAdminScreen(
    navController: NavController,
    loginViewModel: LoginViewModel? = null
) {
    val userState by loginViewModel?.user?.collectAsState(initial = null) ?: remember { mutableStateOf(null) }

    MainLayout (
        showBackButton = false,
        showSearchBar = false,
        showIAButton = false,
        title = if (userState != null) {
            "Welcome, ${userState!!.firstName} ${userState!!.lastName}!"
        } else {
            "Welcome Administrator!"
        },
        height = 0.9f,
        showOptionsAdmin = true,  // New parameter to control the visibility of the options row
        navController = navController,
        showOptionsBottomAdmin = true,
        loginViewModel = loginViewModel
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Add the admin home image
            val image: Painter = painterResource(id = R.drawable.adminhome)
            Image(
                painter = image,
                contentDescription = "Admin Home Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(750.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add a title or prompt
            Text(
                text = "Select an Option Above",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeAdminScreenPreview() {
    DessertSalesAppTheme {
        HomeAdminScreen(navController = rememberNavController())
    }
}