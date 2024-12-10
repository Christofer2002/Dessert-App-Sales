package com.project.dessertapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.project.dessertapp.model.repository.ProductRepository
import com.project.dessertapp.navigation.AppNavigation
import com.project.dessertapp.presentation.ui.theme.DessertSalesAppTheme
import com.project.dessertapp.presentation.viewmodel.CategoryViewModel
import com.project.dessertapp.presentation.viewmodel.EventViewModel
import com.project.dessertapp.presentation.viewmodel.EventsProductsViewModel
import com.project.dessertapp.presentation.viewmodel.OrderViewModel
import com.project.dessertapp.presentation.viewmodel.LoginViewModel
import com.project.dessertapp.presentation.viewmodel.ProductViewModel
import com.project.dessertapp.presentation.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint

class MainActivity : ComponentActivity() {
    private val productViewModel: ProductViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val eventViewModel: EventViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()
    private val eventsProductsViewModel: EventsProductsViewModel by viewModels()
    private val registerViewModel: RegisterViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            DessertSalesAppTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues()) // Añadir padding para los insets de la barra de navegación
                ) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        productViewModel = productViewModel,
                        orderViewModel = orderViewModel,
                        categoryViewModel = categoryViewModel,
                        eventViewModel = eventViewModel,
                        eventsProductsViewModel = eventsProductsViewModel,
                        loginViewModel = loginViewModel,
                        registerViewModel = registerViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun AppPreview() {
    DessertSalesAppTheme {
        val productRepository = ProductRepository
        val navController = rememberNavController()
        val productViewModel = ProductViewModel(productRepository)
        val categoryViewModel = CategoryViewModel()
        val eventViewModel = EventViewModel()
        val events_productsViewModel = EventsProductsViewModel()
        AppNavigation(navController = navController, productViewModel = productViewModel, categoryViewModel = categoryViewModel, eventViewModel = eventViewModel, events_productsViewModel = events_productsViewModel)
    }
}*/
