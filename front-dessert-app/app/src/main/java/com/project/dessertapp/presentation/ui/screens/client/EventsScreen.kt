package com.project.dessertapp.presentation.ui.screens.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.dessertapp.R
import com.project.dessertapp.model.entities.Category
import com.project.dessertapp.model.entities.Event
import com.project.dessertapp.presentation.ui.layouts.MainLayout
import com.project.dessertapp.presentation.ui.theme.ColorBanners
import com.project.dessertapp.presentation.ui.theme.ColorTitle
import com.project.dessertapp.presentation.ui.theme.DefaultBackground
import com.project.dessertapp.presentation.ui.theme.DessertSalesAppTheme
import com.project.dessertapp.presentation.viewmodel.CategoryViewModel
import com.project.dessertapp.presentation.viewmodel.EventViewModel

import androidx.compose.runtime.*

import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.rememberNavController

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.project.dessertapp.presentation.viewmodel.LoginViewModel

@Composable
fun EventsScreen(
    eventViewModel: EventViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel? = null
) {
   // eventsProductsViewModel.loadAllEventProducts()

    // Fetch the list of events when the screen is launched
    LaunchedEffect(Unit) {
        eventViewModel.findAllEvents()
    }

    val eventList by eventViewModel.eventList.collectAsState()

    // State to control the visibility of the dialog
    var showDialog by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<Event?>(null) }

    MainLayout(
        showBackButton = false,
        showSearchBar = true,
        showIAButton = true,
        title = "Recommendations for Events",
        height = 0.9f,
        navController = navController,
        loginViewModel = loginViewModel
    ) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
// Static and centered title
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Events",
                    color = ColorTitle,
                    fontSize = 24.sp, // Increased font size
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Scrollable content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Enable vertical scrolling
            ) {
                RecommendationsSectionE(eventList) { event ->
                    selectedEvent = event
                    showDialog = true
                }
            }
        }
    }

// Mostrar el dialogo si showDialog es true
    if (showDialog && selectedEvent != null) {
        EventDetailDialog(
            event = selectedEvent!!,
            onDismiss = { showDialog = false },
            onRecommendationsClick = {
                navController.navigate("recommendations/${selectedEvent!!.id}")
                showDialog = false
            }
        )
    }
}

@Composable
fun RecommendationsSectionE(eventList: List<Event>, onEventClick: (Event) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(1000.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(60.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight() // Set a fixed height for the grid
        ) {
            items(eventList) { event ->
                RecommendationItemE(event, onEventClick)
            }
        }
    }
}

@Composable
fun RecommendationItemE(event: Event, onEventClick: (Event) -> Unit) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .size(200.dp) // Define the height for the cards
            .padding(horizontal = 16.dp) // Adjust the padding to fit the screen properly
            .clickable { onEventClick(event) } // Handle click event
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .background(Color.Transparent, RoundedCornerShape(15.dp))
                .size(200.dp)
        ) {
            AsyncImage(
                model = event.image,
                contentDescription = "Event Image",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.img), // Recurso local para placeholder
                error = painterResource(id = R.drawable.event), // Recurso local en caso de error
                modifier = Modifier
                    .fillMaxWidth()
                    .size(200.dp)
                    .offset(y = (-40).dp)
                    .zIndex(2f)
            )

            // Gradient overlay at the bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp) // Height of the gradient overlay
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                DefaultBackground,
                                Color(0xFF8B4513) // Darker color at the bottom
                            )
                        ),
                        shape = RoundedCornerShape(15.dp)
                    )
            )

            // Text for event name
            Text(
                text = event.name,
                color = Color(0xfff8e3b6),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun EventDetailDialog(event: Event, onDismiss: () -> Unit,onRecommendationsClick: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(16.dp)
        ) {
            Box {
// Close button (X) in the top-left corner using PNG
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp) // Add some padding to position it correctly
                        .zIndex(1f) // Ensure the X button is on top
                ) {
                    AsyncImage(
                        model = event.image,
                        contentDescription = "Event Image",
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.img),
                        error = painterResource(id = R.drawable.event),
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(200.dp)
                            .offset(y = (-40).dp)
                            .zIndex(2f)
                    )
                }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .zIndex(0f), // Ensure the content is below the X button
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                AsyncImage(
                    model = event.image,
                    contentDescription = "Event Image",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.event),
                    error = painterResource(id = R.drawable.event),
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(200.dp)
                        .offset(y = (-40).dp)
                        .zIndex(2f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = event.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onRecommendationsClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513))
                ) {
                    Text("Recommendations")
                }
            }
            }
        }
    }
}


@Composable
fun SectionTitle(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = ColorTitle,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EventsScreenPreview() {
    val sampleEvents = listOf(
        Event(
            id = 1,
            name = "Birthday Special",
            description = "Celebrate your birthday with our special cakes",
            image = "birthday_special.png"
        ),
        Event(
            id = 2,
            name = "Valentine's Day Offer",
            description = "Sweeten your Valentine's Day with our cupcakes",
            image = "valentines_day_offer.png"
        ),
        Event(
            id = 3,
            name = "Christmas Sale",
            description = "Enjoy the holiday season with our Christmas-themed desserts",
            image = "christmas_sale.png"
        ),
        Event(
            id = 4,
            name = "Summer Special",
            description = "Beat the heat with our refreshing summer treats",
            image = "summer_special.png"
        )
    )

    DessertSalesAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            EventsScreenPreviewContent(sampleEvents)
        }
    }
}

@Composable
fun EventsScreenPreviewContent(eventList: List<Event>) {
    val navController = rememberNavController()

    MainLayout(
        showBackButton = false,
        showSearchBar = true,
        showIAButton = true,
        title = "Welcome, Client!",
        height = 0.9f,
        navController = navController
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            RecommendationsSectionE(eventList) { event ->
                // Handle event click in preview
            }
        }
    }
}
