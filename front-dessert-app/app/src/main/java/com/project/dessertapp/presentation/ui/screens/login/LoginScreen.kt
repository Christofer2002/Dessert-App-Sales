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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.dessertapp.R
import com.project.dessertapp.navigation.NavRoutes
import com.project.dessertapp.presentation.ui.layouts.MainLayout
import com.project.dessertapp.presentation.ui.theme.ColorBanners
import com.project.dessertapp.presentation.viewmodel.CategoryViewModel
import com.project.dessertapp.presentation.viewmodel.LoginViewModel
import com.project.dessertapp.utils.ApplicationInsightsLogger

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel
) {

    MainLayout(
        showBackButton = false,
        showSearchBar = false,
        showIAButton = false,
        title = "Welcome, Login!",
        height = 0.9f,
        showOptionsAdmin = false,
        navController = navController,
        isLoginScreen = true
    ) {
        Column {
            // New promotion banner section
            LoginSection(navController = navController, loginViewModel = loginViewModel)

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}

@Composable
fun LoginSection(
    navController: NavController? = null,
    loginViewModel: LoginViewModel
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Observe the login error and login state
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsStateWithLifecycle()
    val loginError by loginViewModel.loginError.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            Text(text = "Email", fontSize = 20.sp)
        }

        Box {
            TextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Enter a valid email") }
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            Text(text = "Password", fontSize = 20.sp)
        }

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            placeholder = { Text("Enter your password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(16.dp))
        Button(
            onClick = {
                loginViewModel.login(email, password)
                try {
                    val appIn = ApplicationInsightsLogger()
                    appIn.logEvent("LOG-DessertSalesApp-Frontend: User trying to authenticate -> User: $email")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                // Navigate to the next screen on successful login
                if (isLoggedIn) {
                    navController?.let {
                        try {
                            if (loginViewModel.isAdmin()) {
                                navController.navigate(NavRoutes.Admin.route) // Admin Home
                            } else {
                                navController.navigate(NavRoutes.Home.route) // Home
                            }
                        } catch (e: Exception) {
                            Log.e("FME - Execution error", " -> ", e)
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, shape = RoundedCornerShape(25.dp)),
            colors = ButtonDefaults.buttonColors(ColorBanners)
        ) {
            Text(text = "Sign In")
        }

        // Show error message if login fails
        if (loginError != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                loginError!!,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))

        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            ClickableText(
                text = AnnotatedString("Forgot password?"),
                onClick = { /* Handle forgot password */ }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxWidth()
        )
        {

            ClickableText(
                //ClickableText(
                text = AnnotatedString(
                    text = "You haven't yet registered?",
                    spanStyles = listOf(
                        AnnotatedString.Range(
                            item = SpanStyle(
                                color = Color.Black,
                                fontSize = 14.sp,
                                textDecoration = TextDecoration.Underline
                            ),
                            start = 0,
                            end = "You haven't yet registered?".length
                        )
                    )
                ),
                //text = AnnotatedString("You haven't yet registered?"),
                onClick = {
                    /* Handle registration */
                    navController?.let {
                        try {
                            navController.navigate(NavRoutes.Registration.route)
                        } catch (e: Exception) {
                            Log.e("FME - Execution error -> Trying to Registration", " -> ", e)
                        }
                    }
                }

            )
        }

        /////////////////////


        ////////////////////

        Spacer(modifier = Modifier.padding(16.dp))

        /*Button(
            onClick = { *//* Handle Google continue *//* },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, shape = RoundedCornerShape(25.dp)),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.DarkGray,
                containerColor = Color.White
            )

        ) {
            Icon(
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = "Google Logo",
                modifier = Modifier.size(30.dp)
            )
            Text(text = "Continue with Google")
        }*/
    }
}


/*
@Preview(showBackground = true)
@Composable
fun HomeScreenPreviewContent() {
    val navController = rememberNavController()
    val loginViewModel = LoginViewModel()
    LoginScreen(
        navController,
        loginViewModel
    )

}
*/
