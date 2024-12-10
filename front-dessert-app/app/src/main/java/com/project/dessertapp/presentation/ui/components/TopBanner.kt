package com.project.dessertapp.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.dessertapp.model.state.TabSelectionState
import com.project.dessertapp.navigation.NavRoutes
import com.project.dessertapp.presentation.ui.theme.BeigeColor
import com.project.dessertapp.presentation.ui.theme.ColorBanners
import com.project.dessertapp.presentation.ui.theme.DessertSalesAppTheme
import com.project.dessertapp.presentation.viewmodel.LoginViewModel


@Composable
fun TopBanner(
    title: String,
    showBackButton: Boolean = true,
    showSearchBar: Boolean = true,
    showOptionsAdmin: Boolean = false,  // New parameter to control the visibility of the options row
    isAdminHome: Boolean = true,
    navController: NavController? = null,// Lambda to handle option selection,
    onOptionSelected: (String) -> Unit = {},
    tabSelectionState: TabSelectionState = TabSelectionState(isWaitingSelected = true),
    loginViewModel : LoginViewModel? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(ColorBanners) // Background color for the top banner
            .fillMaxWidth() // Make the banner fill the width of the screen
            .padding(vertical = 40.dp), // Vertical padding for the banner
        contentAlignment = Alignment.Center // Align content at the center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth() // Fill the width with the row
                .padding(horizontal = 16.dp), // Horizontal padding
            verticalAlignment = Alignment.CenterVertically, // Vertically center the items in the row
            horizontalArrangement = Arrangement.Start // Arrange items from the start (left)
        ) {
            // Conditionally display the back button
            if (showBackButton) {
                BackButton(navController) // Back button composable
            }

            // Title text with padding only if back button is present
            Text(
                text = title,
                color = BeigeColor, // Global beige color defined in Color.kt
                style = MaterialTheme.typography.titleLarge, // Use the large title typography style
                modifier = Modifier.padding(start = if (showBackButton) 8.dp else 0.dp) // Add padding if back button exists
            )

            Spacer(modifier = Modifier.weight(1f)) // Spacer to push the menu icon to the right

            // Menu icon on the right
            Box {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = BeigeColor
                    )
                }
                CustomDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    navController = navController,
                    loginViewModel = loginViewModel
                )
            }
        }
    }

    if (showSearchBar) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-25).dp)
        ) {
            var searchText = "Cupcakes" // Example text for the search bar
            SearchBar(
                placeholder = "Search for items...",
                searchText = searchText,
                onSearchTextChanged = { searchText = it },
                onSearch = { /* Logic to search */ },
            )
        }
    }

    if (showOptionsAdmin) {
        AdminOptionsRow(
            onOptionSelected = onOptionSelected,
            navController = navController,
            isAdminHome = isAdminHome,
            tabSelectionState = tabSelectionState
        )
    }
}


@Composable
fun BackButton(navController: NavController? = null) {
    // Back button with a round white background and a black arrow icon
    Surface(
        modifier = Modifier.size(40.dp), // Size of the circular button
        shape = CircleShape, // Make the background circular
        color = Color.White, // White background
        shadowElevation = 4.dp // Optional: add a shadow to give it some elevation
    ) {
        IconButton(onClick = {
            navController?.let {
                try {
                    navController.popBackStack()
                } catch (e: Exception) {
                    Log.e("FME - Execution error", " -> ", e)
                }
            }
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Using the ArrowBack icon from Material Icons
                contentDescription = "Back",
                tint = Color.Black, // Black arrow color
                modifier = Modifier.size(24.dp) // Size of the arrow inside the circle
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onSearch: () -> Unit,
    placeholder: String = "Search for Cupcakes, Cakes,..."
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(y = (4).dp)
    ) {
        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier
                .align(Alignment.Center)
                .size(326.dp, 37.dp)
        ) {
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(10.dp),
                        clip = false
                    )
                    .background(Color(0xfff1daae), RoundedCornerShape(10.dp))
                    .fillMaxSize()
            )

            // Icono de filtro
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "Filter",
                tint = Color(0xff6c757d),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
                    .size(24.dp)
            )

            // Campo de texto para la búsqueda
            TextField(
                value = searchText,
                onValueChange = { onSearchTextChanged(it) },
                placeholder = { Text(text = placeholder, color = Color(0xff6c757d)) }, // Asegúrate que el color del placeholder sea visible
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                textStyle = LocalTextStyle.current.copy(
                    color = Color(0xff6c757d),  // Color del texto de búsqueda
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Left
                ),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    cursorColor = Color(0xff6c757d),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            // Icono de búsqueda
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xff6c757d),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 40.dp)
                    .size(16.dp)
            )
        }
    }
}

@Composable
fun AdminOptionsRow(
    onOptionSelected: (String) -> Unit,
    navController: NavController? = null,
    isAdminHome: Boolean = true,
    tabSelectionState: TabSelectionState
) {
    // The row containing the admin buttons must be a `Row` to use `weight`
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)  // No vertical padding
            .background(Color(0xFFF3E5D4)), // Beige background color
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(isAdminHome){
            AdminOptionButton(
                title = "Orders",
                icon = Icons.Default.ShoppingCart,
                isSelectedOption = false,
                modifier = Modifier.weight(1f)
            ) {
                navController?.let {
                    try {
                        navController.navigate(NavRoutes.OrderListAdmin.route)
                    } catch (e: Exception) {
                        Log.e("FME - Execution error", " -> ", e)
                    }
                }
            }

            AdminOptionButton(
                title = "Products",
                icon = Icons.Default.Store,
                isSelectedOption = false,
                modifier = Modifier.weight(1f)
            ) {
                navController?.let {
                    try {
                        navController.navigate(NavRoutes.ProductAdmin.route)
                    } catch (e: Exception) {
                        Log.e("FME - Execution error", " -> ", e)
                    }
                }
            }
        } else{
            AdminOptionButton(
                title = "Waiting",
                icon = Icons.Default.HourglassEmpty,
                isSelectedOption = tabSelectionState.isWaitingSelected,
                modifier = Modifier.weight(1f)
            ) {
                onOptionSelected("Waiting")
            }

            AdminOptionButton(
                title = "Accepted",
                icon = Icons.Default.CheckCircle,
                isSelectedOption = tabSelectionState.isAcceptedSelected,
                modifier = Modifier.weight(1f)
            ) {
                onOptionSelected("Accepted")
            }
            AdminOptionButton(
                title = "Rejected",
                icon = Icons.Default.Cancel,
                isSelectedOption = tabSelectionState.isRejectedSelected,
                modifier = Modifier.weight(1f)
            ) {
                onOptionSelected("Rejected")
            }
        }
    }
}

@Composable
fun AdminOptionButton(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    isSelectedOption: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelectedOption) Color(0xFFF3E5D4) else Color(0xFFE8C88B)
    val textColor = if (isSelectedOption) Color(0xFF693712) else Color(0xFF693712)
    val borderColor = if (isSelectedOption) Color(0xFF693712) else Color(0xFFBCAAA4)

    Surface(
        modifier = modifier
            .height(48.dp)
            .clickable(onClick = onClick)
            .background(backgroundColor)
            .border(1.dp, borderColor, shape = RoundedCornerShape(4.dp)),
        color = Color.Transparent
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp, 24.dp),
                tint = textColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CustomDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    navController: NavController? = null,
    loginViewModel: LoginViewModel? = null
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    val isLoggedIn by loginViewModel?.isLoggedIn?.collectAsState(initial = false) ?: remember { mutableStateOf(false) }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .width(180.dp)
            .offset(x = (-1).dp, y = (-4).dp)
            .background(Color(0xFFF3E5D4), RoundedCornerShape(8.dp))
            .shadow(4.dp, RoundedCornerShape(8.dp))
    ) {

        if (!isLoggedIn) {
            DropdownMenuItem(
                text = { Text("Log in", color = Color(0xFF693712), fontWeight = FontWeight.Bold) },
                onClick = {
                    navController?.let {
                        try {
                            navController.navigate(NavRoutes.Login.route)
                        } catch (e: Exception) {
                            Log.e("FME - Execution error", " -> ", e)
                        }
                    }
                    onDismissRequest()
                },
                modifier = Modifier.border(1.dp, Color(0xFFBCAAA4), RoundedCornerShape(12.dp))
            )
        }

            if(isLoggedIn) {
            DropdownMenuItem(
                text = { Text("Logout", color = Color(0xFF693712), fontWeight = FontWeight.Bold) },
                onClick = {
                    loginViewModel?.logout()
                    showLogoutDialog = true
                },
                modifier = Modifier
                    .border(1.dp, Color(0xFFBCAAA4), RoundedCornerShape(12.dp))
            )
        }
    }
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(text = "Logout succesful") },
            text = { Text(text = "You have successfully logged out.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false

                        navController?.let {
                            try {
                                navController.navigate(NavRoutes.Login.route)
                            } catch (e: Exception) {
                                Log.e("FME - Execution error", " -> ", e)
                            }
                        }}
                ) {
                    Text("OK")
                }
            },
            shape = RoundedCornerShape(12.dp)
        )
    }
}

/*// Preview with the back button visible
@Preview(showBackground = true)
@Composable
fun TopBannerWithBackButtonPreview() {
    DessertSalesAppTheme {
        val navController = rememberNavController()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background // Set the background color
        ) {
            Column {
                TopBanner(
                    title = "Your Products", // Set the title for the banner
                    showBackButton = true, // Show the back button
                    showSearchBar = false, // Show the search bar
                    navController = navController
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBannerWithoutBackButtonPreview() {
    DessertSalesAppTheme {
        val navController = rememberNavController()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background // Set the background color
        ) {
            Column {
                TopBanner(
                    title = "Welcome App", // Set the title for the banner
                    showBackButton = false, // Do not show the back button
                    showSearchBar = true, // Show the search bar
                    navController = navController
                )
            }
        }
    }
}*/

/*@Preview(showBackground = true)
@Composable
fun TopBannerWithAdminOptionsPreview() {
    DessertSalesAppTheme {
        val navController = rememberNavController()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background // Set the background color
        ) {
            Column {
                TopBanner(
                    title = "Welcome, Administrator!", // Set the title for the banner
                    showBackButton = false, // Hide the back button
                    showSearchBar = false, // Hide the search bar
                    showOptionsAdmin = true, // Show Admin Options (Orders and Products)
                    navController = navController
                )
            }
        }
    }
}*/

