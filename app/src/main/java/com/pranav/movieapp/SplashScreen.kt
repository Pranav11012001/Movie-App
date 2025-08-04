package com.pranav.movieapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    // Replace R.drawable.your_splash_image with your actual drawable resource
    val context = LocalContext.current
    val imageRes = context.resources.getIdentifier(
        "splashscreen",
        "drawable",
        context.packageName
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Splash Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

    LaunchedEffect(Unit) {
        delay(2000) // Show splash for 3 seconds
        navController.navigate("home") {
            popUpTo("splash") { inclusive = true }
        }
    }
}