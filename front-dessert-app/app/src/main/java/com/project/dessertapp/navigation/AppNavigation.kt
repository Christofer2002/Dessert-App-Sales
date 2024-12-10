package com.project.dessertapp.navigation


//import com.project.dessertapp.presentation.ui.screens.admin.EditProductScreen
import com.project.dessertapp.R

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.project.dessertapp.presentation.ui.screens.admin.HomeAdminScreen
import com.project.dessertapp.presentation.ui.screens.admin.ManageProductScreen
import com.project.dessertapp.presentation.ui.screens.admin.ProductAdminScreen
import com.project.dessertapp.presentation.ui.screens.client.EventsScreen
import com.project.dessertapp.presentation.ui.screens.client.HomeScreen
import com.project.dessertapp.presentation.ui.screens.client.CakeScreen
import com.project.dessertapp.presentation.ui.screens.client.MoreDetailsScreen
import com.project.dessertapp.presentation.ui.screens.client.ProductDetailScreen
import com.project.dessertapp.presentation.ui.screens.login.LoginScreen
import com.project.dessertapp.presentation.ui.screens.login.RegistrationScreen
import com.project.dessertapp.presentation.viewmodel.CategoryViewModel
import com.project.dessertapp.presentation.viewmodel.EventViewModel
import com.project.dessertapp.presentation.viewmodel.EventsProductsViewModel
import com.project.dessertapp.presentation.viewmodel.ProductViewModel
import androidx.navigation.NavType
import com.project.dessertapp.model.entities.Product
import com.project.dessertapp.presentation.ui.screens.admin.OrderListAdminScreen
import com.project.dessertapp.presentation.ui.screens.admin.OrderListDetailAdminScreen
import com.project.dessertapp.presentation.ui.screens.client.OrderListScreen
import com.project.dessertapp.presentation.viewmodel.OrderViewModel
import com.project.dessertapp.presentation.viewmodel.LoginViewModel
import com.project.dessertapp.presentation.viewmodel.RegisterViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    productViewModel: ProductViewModel,
    orderViewModel: OrderViewModel,
    categoryViewModel: CategoryViewModel,
    eventViewModel: EventViewModel,
    eventsProductsViewModel: EventsProductsViewModel,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    modifier: Modifier = Modifier
) {

    NavHost(navController = navController, startDestination = NavRoutes.Login.route) {
        composable(NavRoutes.Home.route) {
            HomeScreen(
                categoryViewModel = categoryViewModel,
                eventViewModel = eventViewModel,
                navController = navController,
                modifier = modifier,
                loginViewModel = loginViewModel
            )
        }
        composable(NavRoutes.Event.route) {
            EventsScreen(
                eventViewModel = eventViewModel,
                navController = navController,
                modifier = modifier,
                loginViewModel = loginViewModel
            )
        }
        composable(
            route = NavRoutes.ProductDetail.route,
            arguments = listOf(navArgument("categoryId") { defaultValue = "0" })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toLongOrNull() ?: 0
            ProductDetailScreen(
                categoryId = categoryId,
                eventId = null,
                productViewModel = productViewModel,
                navController = navController,
                loginViewModel = loginViewModel
            )
        }
        composable(NavRoutes.Login.route) {
            LoginScreen(navController = navController,
                loginViewModel = loginViewModel)
        }

        composable(NavRoutes.Registration.route) {
            RegistrationScreen(navController = navController, registerViewModel)
        }

        composable(NavRoutes.Admin.route) {
            HomeAdminScreen(navController = navController, loginViewModel = loginViewModel)
        }
        composable(NavRoutes.ProductAdmin.route) {
            ProductAdminScreen(
                productViewModel = productViewModel,
                navController = navController,
                loginViewModel = loginViewModel
            )
        }
        composable(
            route = NavRoutes.ManageProductAdmin.route,
            arguments = listOf(navArgument("productId") { defaultValue = "0" })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toLongOrNull()
            ManageProductScreen(
                productId = if (productId == 0L) null else productId,
                productViewModel = productViewModel,
                categoryViewModel = categoryViewModel,
                navController = navController,
                loginViewModel = loginViewModel
            )
        }
        // New composable for the RecommendationsScreen
        composable(
            route = NavRoutes.RecommendationsScreen.route,
            arguments = listOf(navArgument("eventId") { defaultValue = "0" })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")?.toLongOrNull() ?: 0
            ProductDetailScreen(
                categoryId = null,
                eventId = eventId,
                productViewModel = productViewModel,
                navController = navController,
                loginViewModel = loginViewModel
            )
        }
        composable(route = NavRoutes.ShoppingCart.route) {
            OrderListScreen(
                orderViewModel = orderViewModel,
                modifier = modifier,
                navController = navController,
                loginViewModel = loginViewModel
            )
        }

        composable(
            route = NavRoutes.CakeScreen.route,
            arguments = listOf(navArgument("productId") { defaultValue = "0" })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toLongOrNull() ?: 0
            CakeScreen(
                navController = navController, productViewModel, orderViewModel, productId, loginViewModel = loginViewModel
            )
        }

        // Add new composable for the MoreDetailsScreen
        composable(
            route = NavRoutes.MoreDetailsScreen.route,
            arguments = listOf(navArgument("productId") { defaultValue = "0" })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toLongOrNull() ?: 0
            MoreDetailsScreen(
                navController = navController, productViewModel = productViewModel, productId, orderViewModel = orderViewModel, loginViewModel = loginViewModel)
        }
        composable(NavRoutes.OrderListAdmin.route) {
            OrderListAdminScreen(
                orderViewModel = orderViewModel,
                navController = navController,
                loginViewModel = loginViewModel
            )
        }
        composable(
            route = NavRoutes.OrderListDetailAdmin.route,
            arguments = listOf(navArgument("orderId") { type = NavType.LongType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getLong("orderId") ?: 0L
            OrderListDetailAdminScreen(orderId = orderId, orderViewModel = orderViewModel, productViewModel = productViewModel, navController = navController, loginViewModel = loginViewModel)
        }
    }
}

