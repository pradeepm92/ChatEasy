package com.example.chateasy.auth.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chateasy.R
import com.example.chateasy.graphs.AuthScreen

import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate(AuthScreen.Login.route) {
            popUpTo(AuthScreen.SplashScreen.route) { inclusive = true }
        }
    }

    // SplashScreen UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.chat),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(150.dp)
                    // Use size for both width and height
                    .clip(CircleShape) // Clip the image to be circular
                    .border(2.dp, Color.Gray, CircleShape) // Optional: Add a border if you like

            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Chat Easy",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold, // Use predefined weights like SemiBold or Bold
                fontStyle = FontStyle.Normal ,
                color = colorResource(id = R.color.md_green_900)
                // You can use FontStyle.Italic or FontStyle.Normal
            )

        }


    }
}