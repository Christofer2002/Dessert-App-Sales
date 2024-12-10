package com.project.dessertapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.project.dessertapp.R

sealed class NavRoutes(val route: String, val title: Int, val icon: ImageVector) {
    data object Home : NavRoutes("home", R.string.home, Icons.Default.Home)
    data object Event : NavRoutes("event", R.string.event, Icons.Default.Event)
    data object ShoppingCart : NavRoutes("shoppingCart", R.string.shopping_cart, Icons.Default.ShoppingCart)
    data object Login : NavRoutes("login", R.string.login, Icons.Default.Person)
    data object Registration : NavRoutes("registration", R.string.registration, Icons.Default.HowToReg)
    data object Admin : NavRoutes("admin", R.string.admin, Icons.Default.Person)
    data object ProductAdmin : NavRoutes("product", R.string.product, Icons.Default.Person)
    data object ProductEditAdmin : NavRoutes("product/{productId}", R.string.product_edit, Icons.Default.Person)
    data object OrderListAdmin : NavRoutes("order-list-admin", R.string.order_list_admin, Icons.Default.Person)
    data object OrderListDetailAdmin : NavRoutes("order-list-detail-admin/{orderId}", R.string.order_list_detail_admin, Icons.Default.Person)
    data object ProductDetail : NavRoutes("detail/{categoryId}", R.string.product_detail, Icons.Default.Home)
    data object CakeScreen : NavRoutes("productCake/{productId}", R.string.dessert_screen, Icons.Default.Home)
    data object ManageProductAdmin : NavRoutes("product/{productId}", R.string.product_management, Icons.Default.Person)
    data object MoreDetailsScreen : NavRoutes("more-detail-screen/{productId}", R.string.more_details_screen, Icons.Default.Home)
    data object RecommendationsScreen : NavRoutes("recommendations/{eventId}", R.string.recommendations_screen, Icons.Default.Home)
}
