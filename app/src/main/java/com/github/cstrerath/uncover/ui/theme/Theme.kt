package com.github.cstrerath.uncover.ui.theme

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    background = Color(0xFF121212),
    surface = Color(0xFF121212)
)

private val LightColors = lightColorScheme(
    background = Color.White,
    surface = Color.White
)

@Composable
fun UncoverTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    Log.d(TAG, "Applying theme with darkTheme: $darkTheme")
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}

private const val TAG = "UncoverTheme"
