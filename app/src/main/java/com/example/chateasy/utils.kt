package com.example.chateasy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.lang.reflect.Modifier
import androidx.compose.ui.draw.clip


object utils {
    val buttonGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF6A1B9A), // Purple
            Color(0xFFD32F2F)  // Red
        )
    )
    @Composable
    fun GradientButton(
        text: String,
        onClick: () -> Unit,
        gradientColors: Brush,
        modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
    ) {


        Box(
            modifier = modifier
                .background(gradientColors, shape = RoundedCornerShape(8.dp)) // Gradient background
                .clickable(onClick = onClick) // Handle clicks
                .padding(10.dp),
            contentAlignment = Alignment.Center// Padding to avoid clipping
        ) {
            Text(text = text, color = Color.White)
        }
    }
}