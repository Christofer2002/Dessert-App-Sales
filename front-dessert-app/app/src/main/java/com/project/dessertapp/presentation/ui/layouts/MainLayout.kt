package com.project.dessertapp.presentation.ui.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.dessertapp.model.state.TabSelectionState
import com.project.dessertapp.presentation.ui.components.BottomBar
import com.project.dessertapp.presentation.ui.components.TopBanner
import com.project.dessertapp.presentation.ui.theme.DessertSalesAppTheme
import com.project.dessertapp.presentation.viewmodel.LoginViewModel

@Composable
fun MainLayout(
    showBackButton: Boolean = false,
    showSearchBar: Boolean = true,
    showIAButton: Boolean = true,
    title: String = "Welcome, Client!",
    height: Float = 1f,
    showOptionsAdmin: Boolean = false,  // New parameter to control the visibility of the options row
    navController: NavController,
    loginViewModel: LoginViewModel? = null,
    showOptionsBottomAdmin: Boolean = false,
    isAdminHome : Boolean = true,
    isLoginScreen : Boolean = false,
    onOptionSelected: (String) -> Unit = { _ -> },
    tabSelectionState: TabSelectionState = TabSelectionState(isWaitingSelected = true), // New parameter to store the tab selection state
    content: @Composable () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        onOptionSelected("Waiting")
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize() // Ensure the column takes up the full size
        ) {
            // Client-specific top bar
            TopBanner(
                title = title,
                showBackButton = showBackButton,
                showSearchBar = showSearchBar,
                showOptionsAdmin = showOptionsAdmin,
                isAdminHome = isAdminHome,
                navController = navController,
                onOptionSelected = onOptionSelected,
                tabSelectionState = tabSelectionState,
                loginViewModel = loginViewModel
            )

            // Main content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(height) // Control the space used by the main content
            ) {
                content() // Insert the content passed from outside here
            }

            // Client-specific bottom navigation bar
            BottomBar(
                showIAButton = showIAButton,
                navController = navController,
                showOptionsBottomAdmin = showOptionsBottomAdmin,
                isLoginScreen = isLoginScreen
            )
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun ClientLayoutPreview() {
    DessertSalesAppTheme {
        val navController = rememberNavController() // Añadir NavController simulado
        MainLayout(
            navController = navController // Pasar el NavController simulado
        ) {
            // Preview content
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text("This is a preview content", style = MaterialTheme.typography.titleLarge)
                Text("Here goes more content", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
*/

