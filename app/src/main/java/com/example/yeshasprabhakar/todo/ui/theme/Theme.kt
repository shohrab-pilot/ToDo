package com.example.yeshasprabhakar.todo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(0xFFBB86FC), // Purple 200
    primaryVariant = Color(0xFF3700B3), // Purple 700
    secondary = Color(0xFF03DAC6), // Teal 200
    background = Color(0xFF121212), // Dark background
    surface = Color(0xFF121212), // Dark surface
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorPalette = lightColors(
    primary = Color(0xFF6200EE), // Purple 500
    primaryVariant = Color(0xFF3700B3), // Purple 700
    secondary = Color(0xFF03DAC6), // Teal 200
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun TodoTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography, // Assuming Typography.kt will be created later or exists
        shapes = Shapes,     // Assuming Shapes.kt will be created later or exists
        content = content
    )
}
