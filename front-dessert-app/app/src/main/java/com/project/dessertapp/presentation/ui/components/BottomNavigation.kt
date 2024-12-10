package com.project.dessertapp.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.dessertapp.model.entities.Product
import com.project.dessertapp.navigation.NavRoutes
import com.project.dessertapp.presentation.ui.theme.BeigeColor
import com.project.dessertapp.presentation.ui.theme.ColorBanners
import com.project.dessertapp.presentation.ui.theme.DessertSalesAppTheme
import com.project.dessertapp.presentation.viewmodel.OrderViewModel
import com.project.dessertapp.presentation.viewmodel.ProductViewModel
import okhttp3.internal.concurrent.formatDuration

@Composable
fun BottomBar(
    navController: NavController,
    showIAButton: Boolean = false,
    showOptionsBottomAdmin: Boolean = false,
    isLoginScreen: Boolean = false
) {
    val customShape: Shape = if (showIAButton) {
        // Custom shape with a curve for the AI button
        GenericShape { size, _ ->
            val width = size.width
            val height = size.height
            moveTo(0f, 0f)
            lineTo(width * 0.37f, 0f)
            cubicTo(
                width * 0.42f, 0f,
                width * 0.42f, height * 0.55f,
                width * 0.5f, height * 0.53f
            )
            cubicTo(
                width * 0.58f, height * 0.55f,
                width * 0.58f, 0f,
                width * 0.63f, 0f
            )
            lineTo(width, 0f)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }
    } else {
        // Custom shape without a curve for the AI button
        RoundedCornerShape(0.dp)
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Bottom bar content
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(ColorBanners, shape = customShape), // Apply the custom shape
            color = Color.Transparent
        ) {
            if (!isLoginScreen) {
                if (showOptionsBottomAdmin) {
                    // Show only Home icon centered when showOptionsAdmin is true
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = NavRoutes.Home.icon,
                            contentDescription = "Home",
                            tint = BeigeColor,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable {
                                    navController.navigate(NavRoutes.Admin.route)
                                }
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp), // General padding
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Row for left icons
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(45.dp), // Spacing between left icons
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = NavRoutes.Home.icon,
                                contentDescription = "Home",
                                tint = BeigeColor,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable {
                                        navController.navigate(NavRoutes.Home.route)  // Navegate to the Home Screen
                                    }
                            )
                            Icon(
                                imageVector = NavRoutes.Event.icon,
                                contentDescription = "Event",
                                tint = BeigeColor,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable {
                                        navController.navigate(NavRoutes.Event.route)  // Navegate to the Event Screen
                                    }
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f)) // Space between the two rows

                        // Row for right icons
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(45.dp), // Spacing between right icons
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = NavRoutes.ShoppingCart.icon,
                                contentDescription = "Shopping Cart",
                                tint = BeigeColor,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable {
                                        navController.navigate("shoppingCart")
                                        Log.i(
                                            "FME Log -> ",
                                            "BottonNavigation - Trying to load OrderListScreen"
                                        )
                                    }
                            )
                            Icon(
                                imageVector = NavRoutes.Login.icon,
                                contentDescription = "Profile",
                                tint = BeigeColor,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable {
                                        navController.navigate(NavRoutes.Login.route)  // Navigate to the Login Screen
                                    }
                            )
                        }
                    }
                }
            }
        }

        // IA Button (Optional)
        if (showIAButton) {
            Surface(
                modifier = Modifier
                    .size(64.dp)
                    .offset(y = (-32).dp), // Place it just above the BottomBar
                shape = CircleShape,
                color = ColorBanners,
                shadowElevation = 8.dp
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "AI", color = Color.White)
                }
            }
        }
    }
}

