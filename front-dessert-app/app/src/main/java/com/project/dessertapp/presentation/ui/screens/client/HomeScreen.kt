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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import coil.compose.AsyncImage
import com.project.dessertapp.R
import com.project.dessertapp.model.entities.Category
import com.project.dessertapp.model.entities.Event
import com.project.dessertapp.navigation.NavRoutes
import com.project.dessertapp.presentation.ui.layouts.MainLayout
import com.project.dessertapp.presentation.ui.theme.ColorBanners
import com.project.dessertapp.presentation.ui.theme.ColorTitle
import com.project.dessertapp.presentation.ui.theme.DefaultBackground
import com.project.dessertapp.presentation.ui.theme.DessertSalesAppTheme
import com.project.dessertapp.presentation.viewmodel.CategoryViewModel
import com.project.dessertapp.presentation.viewmodel.EventViewModel
import com.project.dessertapp.presentation.viewmodel.LoginViewModel
import com.project.dessertapp.utils.ApplicationInsightsLogger

@Composable
fun HomeScreen(
    categoryViewModel: CategoryViewModel,
    eventViewModel: EventViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel? = null
) {
    val appIn: ApplicationInsightsLogger = ApplicationInsightsLogger()
    categoryViewModel.findAllCategories()
    eventViewModel.findAllEvents()

    // Fetch the list of events when the screen is launched
    LaunchedEffect(Unit) {
        eventViewModel.findAllEvents()
    }

    val categoryList by categoryViewModel.categoryList.collectAsState()
    val eventList by eventViewModel.eventList.collectAsState()

    val userState by loginViewModel?.userResponse?.collectAsState(initial = null) ?: remember { mutableStateOf(null) }

    MainLayout (
        showBackButton = false,
        showSearchBar = true,
        showIAButton = false,
        title = if (userState != null) "Welcome, ${userState!!.firstName} ${userState!!.lastName}!" else "Welcome, Client!",
        height = 0.9f,
        navController = navController,
        loginViewModel = loginViewModel
    ){
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Enable vertical scrolling
        ) {

            appIn.logEvent("FME, the mobile app just started the Home view.")
            // New promotion banner section
            PromotionSection()

            CategoriesSection(categoryList, navController)

            Spacer(modifier = Modifier.height(16.dp))

            RecommendationsSection(eventList, navController)
        }
    }
}


@Composable
fun PromotionSection() {
    // Center the banner and image
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth() // Fill the width of the screen
    ) {
        // The cake image and the text box
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(326.dp, 140.dp),
        ) {
            // Box for shadow and background of the banner
            Box(
                modifier = Modifier
                    .align(Alignment.Center) // Align the box in the center
                    .background(ColorBanners, RoundedCornerShape(15.dp)) // Background and shape
                    .size(326.dp, 67.dp),
            )

            // Promotional text
            Text(
                modifier = Modifier
                    .align(Alignment.Center) // Align the text in the center
                    .wrapContentHeight()
                    .offset(x = 30.dp), // Adjust the offset to center the text horizontally
                text = "Your code: Disc20\n20% off on all orders",
                color = Color(0xfffcf2d9),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
            )

            // Image of the cupcake
            Image(
                painter = painterResource(id = R.drawable.img_discount),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .align(Alignment.TopStart) // Align the image at the top
                    .offset(
                        x = (-1).dp,
                        y = (-20).dp
                    ) // Adjust the offset to place the image properly
                    .size(115.dp, 140.dp), // Adjust the size of the image
            )
        }
    }
}


@Composable
fun CategoriesSection(categoryList: List<Category>, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Categories",
            color = ColorTitle,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(categoryList) { category ->
            CategoryItem(category) {
                // Navigate to the detail screen when a category is clicked
                navController.navigate("detail/${category.id}")
            }
        }
    }
}

@Composable
fun CategoryItem(category: Category, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(82.dp, 100.dp) // Set the overall size of the item
            .padding(8.dp) // Inner padding
            .clickable(onClick = onClick),
        contentAlignment = Alignment.BottomCenter // Align content towards the bottom
    ) {
        // Gradient box at the bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp) // Set the height of the box to only cover the lower part
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            DefaultBackground, // Darker brown
                            Color(0xFF8B4513)  // Lighter beige
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .zIndex(1f) // Ensure the box stays behind the image
        ) {
            Text(
                text = category.name,
                color = Color(0xfff8e3b6),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter) // Align the text in the center of the box at the bottom
                    .padding(bottom = 4.dp) // Add padding so the text is not too close to the edge
            )
        }

        AsyncImage(
            model = category.image,
            contentDescription = "Category Image",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.img),
            error = painterResource(id = R.drawable.img),
            modifier = Modifier
                .size(64.dp)
                .offset(y = (-27).dp)
                .zIndex(2f)
        )
    }
}

@Composable
fun RecommendationsSection(eventList: List<Event>, navController: NavController) {
    var selectedEvent by remember { mutableStateOf<Event?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(400.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Recommendations for events",
                color = ColorTitle,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = "See all",
                color = ColorTitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .clickable(onClick = { navController.navigate(NavRoutes.Event.route) })
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(60.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {
            items(eventList) { event ->
                RecommendationItem(event = event) {
                    selectedEvent = event
                    showDialog = true
                }
            }
        }
    }

    // Mostrar el diálogo si showDialog es verdadero
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
fun RecommendationItem(event: Event, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .size(200.dp)
            .padding(horizontal = 16.dp)
            .clickable { onClick() } // Detectar el clic en el evento
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                DefaultBackground,
                                Color(0xFF8B4513)
                            )
                        ),
                        shape = RoundedCornerShape(15.dp)
                    )
            )

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


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val sampleCategories = listOf(
        Category(id = 1, name = "Cupcake", image = "cupcake.png"),
        Category(id = 2, name = "Cakes", image = "cake.png"),
        Category(id = 3, name = "Donuts", image = "donut.png"),
        Category(id = 4, name = "Brownies", image = "brownie.png"),
    )

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
            HomeScreenPreviewContent(sampleCategories, sampleEvents)
        }
    }
}

@Composable
fun HomeScreenPreviewContent(categoryList: List<Category>, eventList: List<Event>) {
    val navController = rememberNavController()

    MainLayout (
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
           // PromotionSection()

            Spacer(modifier = Modifier.height(16.dp))

            CategoriesSection(categoryList, navController)

            Spacer(modifier = Modifier.height(16.dp))

            RecommendationsSection(eventList, navController)
        }
    }
}

