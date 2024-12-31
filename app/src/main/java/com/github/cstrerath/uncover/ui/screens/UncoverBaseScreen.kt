package com.github.cstrerath.uncover.ui.screens

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private const val TAG = "UncoverBaseScreen"

@Composable
fun UncoverBaseScreen(
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val backgroundColor = MaterialTheme.colorScheme.background
    val darkTheme = isSystemInDarkTheme()

    Log.d(TAG, "Setting up base screen with darkTheme: $darkTheme")

    DisposableEffect(systemUiController, darkTheme) {
        Log.d(TAG, "Updating system UI colors")
        systemUiController.setStatusBarColor(
            color = backgroundColor,
            darkIcons = !darkTheme
        )
        onDispose {
            Log.d(TAG, "Cleaning up system UI controller")
        }
    }

    content()

}

