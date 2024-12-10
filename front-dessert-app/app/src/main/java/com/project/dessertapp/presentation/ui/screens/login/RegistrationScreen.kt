package com.project.dessertapp.presentation.ui.screens.login

import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.project.dessertapp.model.entities.Taste
import com.project.dessertapp.model.entities.User
import com.project.dessertapp.navigation.NavRoutes
import com.project.dessertapp.presentation.ui.layouts.MainLayout
import com.project.dessertapp.presentation.viewmodel.RegisterViewModel
import com.project.dessertapp.utils.ApplicationInsightsLogger
import java.util.Date

@Composable
fun RegistrationScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel,
    modifier: Modifier = Modifier
) {
    MainLayout(
        showBackButton = true,
        showSearchBar = false,
        showIAButton = false,
        title = "Welcome, registration!",
        height = 0.9f,
        showOptionsAdmin = false,
        isLoginScreen = true,
        navController = navController
    ) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Enable vertical scrolling
        ) {

            // New promotion banner section
            RegistrationSection(
                navController = navController,
                registerViewModel = registerViewModel
            )
            PopupDialogExample(navController = navController, registerViewModel)
            //Spacer(modifier = Modifier.height(16.dp))

        }
    }
}

@Composable
fun RegistrationSection(
    navController: NavController? = null,
    registerViewModel: RegisterViewModel
) {
    LaunchedEffect(key1 = true) {
        registerViewModel.getTastes()
    }

    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val tastesState by registerViewModel.tastes.collectAsState(initial = emptyList())
    var addedTastes by remember { mutableStateOf(listOf<Taste>()) }

    val isLoggedIn by registerViewModel.isLoggedIn.collectAsState()
    val loginError by registerViewModel.loginError.collectAsState()

    // Control del dialog
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Name field
        InputField(
            label = "Name",
            value = name,
            onValueChange = { name = it },
            placeholder = "Enter your name"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lastname field
        InputField(
            label = "Lastname",
            value = lastname,
            onValueChange = { lastname = it },
            placeholder = "Enter your lastname"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email field
        InputField(
            label = "Email",
            value = email,
            onValueChange = { email = it },
            placeholder = "Enter a valid email"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown Section for Tastes
        tastesState?.let {
            MultiSelectTastesDropdown(
                tastesState = it,
                addedTastes = addedTastes,
                onTasteAdded = { taste ->
                    if (taste !in addedTastes) {
                        addedTastes = addedTastes + taste
                    }
                },
                onTasteRemoved = { taste ->
                    addedTastes = addedTastes - taste
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Password field
        PasswordField(
            label = "Password",
            password = password,
            onValueChange = { password = it },
            passwordVisible = passwordVisible,
            onVisibilityChange = { passwordVisible = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm password field
        PasswordField(
            label = "Confirm Password",
            password = confirmPassword,
            onValueChange = { confirmPassword = it },
            passwordVisible = passwordVisible,
            onVisibilityChange = { passwordVisible = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Show error message if register fails
        loginError?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Save data and validate
        Button(
            onClick = {
                if (validateForm(name, lastname, email, password, confirmPassword, addedTastes)) {
                    val user = User(
                        id = 0,
                        createdDate = Date(),
                        email = email,
                        firstName = name,
                        lastName = lastname,
                        password = password,
                    )
                    registerViewModel.register(user, addedTastes)
                    try {
                        val appIn = ApplicationInsightsLogger()
                        appIn.logEvent("LOG-DessertSalesApp-Frontend: New user created -> User: $name, Email: $email")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    registerViewModel.setError("Please fill in all the required fields correctly.")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color(0xFF6B8E23))
        ) {
            Text("Register")
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Show dialog if registration is successful
    if (isLoggedIn && !showDialog) {
        showDialog = true
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(text = "Registration Successful") },
            text = { Text("Your account has been created successfully!") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        registerViewModel.setLoggedIn(false)
                        navController?.navigate("login") // Navigate to the login screen
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

// Composable for Input Fields
@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String) {
    Column {
        Text(label, fontSize = 20.sp, modifier = Modifier.align(Alignment.Start))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) }
        )
    }
}

// Composable for Password Fields
@Composable
fun PasswordField(
    label: String,
    password: String,
    onValueChange: (String) -> Unit,
    passwordVisible: Boolean,
    onVisibilityChange: (Boolean) -> Unit
) {
    Column {
        Text(label, fontSize = 20.sp, modifier = Modifier.align(Alignment.Start))
        TextField(
            value = password,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { Text("Enter your $label") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { onVisibilityChange(!passwordVisible) }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun MultiSelectTastesDropdown(
    tastesState: List<Taste>,
    addedTastes: List<Taste>,
    onTasteAdded: (Taste) -> Unit,
    onTasteRemoved: (Taste) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
        ) {
            Button(
                onClick = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select your tastes")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                tastesState.forEach { taste ->
                    DropdownMenuItem(
                        onClick = {
                            onTasteAdded(taste)
                            expanded = false
                        },
                        text = { Text(text = taste.name) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Selected Tastes:", fontSize = 18.sp)
        addedTastes.forEach { taste ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = taste.name, fontSize = 16.sp)
                Button(
                    onClick = { onTasteRemoved(taste) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.size(80.dp, 36.dp)
                ) {
                    Text("Remove", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun PopupDialogExample(navController: NavController?, registerViewModel: RegisterViewModel) {
    val openDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Registration success!")
            },
            text = {
                Text("You can now proceed and Log In")
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        navController?.let {
                            try {
                                navController.navigate(NavRoutes.Login.route)
                            } catch (e: Exception) {
                                Log.e("FME - Execution error", " -> ", e)
                            }
                        }
                    }
                ) {
                    Text("Sign In")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("Cancelar")
                }
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        )
    }

}

// Form validation
fun validateForm(
    name: String,
    lastname: String,
    email: String,
    password: String,
    confirmPassword: String,
    tastes: List<Taste>
): Boolean {
    return name.isNotEmpty() &&
            lastname.isNotEmpty() &&
            email.isNotEmpty() &&
            password.isNotEmpty() &&
            password == confirmPassword &&
            tastes.isNotEmpty()
}


/*
@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreviewContent() {
    val navController = rememberNavController()
    val registerViewModel = RegisterViewModel()
    RegistrationSection(
        navController,
        registerViewModel
    )
    //PopupDialogExample(navController = null)
}*/
