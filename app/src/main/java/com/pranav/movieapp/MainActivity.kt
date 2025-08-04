package com.pranav.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pranav.movieapp.getmovies.HomeViewModel
import com.pranav.movieapp.ui.theme.MovieAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieAppTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val viewModel: HomeViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("home") {
            AppScaffold(navController = navController) {
                HomeScreen(navController, viewModel)
            }
        }
        composable("details/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull() ?: return@composable
            AppScaffold(navController = navController) {
                MovieDetailScreen(movieId)
            }
        }
        composable("now_playing") {
            AppScaffold(navController = navController) {
                NowPlayingScreen(navController)
            }
        }
        composable("popular") {
            AppScaffold(navController = navController) {
                PopularScreen(navController)
            }
        }
        composable("top_rated") {
            AppScaffold(navController = navController) {
                TopRatedScreen(navController)
            }
        }
        composable("upcoming") {
            AppScaffold(navController = navController) {
                UpcomingScreen(navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val currentRoute = navController.currentDestination?.route
    val title = when (currentRoute) {
        "home" -> "Home"
        "now_playing" -> "Now Playing"
        "popular" -> "Popular"
        "top_rated" -> "Top Rated"
        "upcoming" -> "Upcoming"
        "details/{movieId}" -> "Details"
        else -> "Movie App" // Fallback title
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // App Title (non-clickable)
                Text(
                    text = "Movie App",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp),
                    onTextLayout = {}
                )

                // User Profile Section
                Text(
                    text = "Rahul Laddar",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                    onTextLayout = {}
                )

                Divider(
                    modifier = Modifier.padding(bottom = 16.dp)
                )



                // Navigation Items
                NavigationDrawerItem(
                    label = { Text("Home", onTextLayout = {} ) },
                    selected = navController.currentDestination?.route == "home",
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("home") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    interactionSource = remember { MutableInteractionSource() } // Add this line
                )

                // Now Playing
                NavigationDrawerItem(
                    label = { Text("Now Playing",  onTextLayout = {}) },
                    selected = navController.currentDestination?.route == "now_playing",
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("now_playing") {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = null) },
                    interactionSource = remember { MutableInteractionSource() }
                )

                // Popular
                NavigationDrawerItem(
                    label = { Text("Popular",  onTextLayout = {}) },
                    selected = navController.currentDestination?.route == "popular",
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("popular") {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Star, contentDescription = null) },
                    interactionSource = remember { MutableInteractionSource() }
                )

                // Top Rated
                NavigationDrawerItem(
                    label = { Text("Top Rated",  onTextLayout = {}) },
                    selected = navController.currentDestination?.route == "top_rated",
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("top_rated") {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.ThumbUp, contentDescription = null) },
                    interactionSource = remember { MutableInteractionSource() }
                )

               // Upcoming
                NavigationDrawerItem(
                    label = { Text("Upcoming",  onTextLayout = {}) },
                    selected = navController.currentDestination?.route == "upcoming",
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("upcoming") {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                    interactionSource = remember { MutableInteractionSource() }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = title,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            onTextLayout = {}
                            // Removed onTextLayout as it's not needed here
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                }
                            },
                            interactionSource = remember { MutableInteractionSource() } // Add this
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { /* Handle profile click */ },
                            interactionSource = remember { MutableInteractionSource() } // Add this
                        ) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF81D4FA)
                    )
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                content()
            }
        }
    }
}