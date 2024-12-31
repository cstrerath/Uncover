package com.github.cstrerath.uncover.ui.screens.main

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.github.cstrerath.uncover.domain.auth.LoginManager
import com.github.cstrerath.uncover.utils.navigation.NavigationManager
import kotlinx.coroutines.delay

private const val TAG = "LaunchScreen"
private const val SPLASH_DELAY = 2000L

@Composable
fun LaunchScreen(
    isInitialLaunch: Boolean,
    loginManager: LoginManager,
    navigationManager: NavigationManager,
    onFinish: () -> Unit
) {
    Log.d(TAG, "Initializing launch screen with isInitialLaunch: $isInitialLaunch")

    if (isInitialLaunch) {
        SplashScreen()
    }

    LaunchedEffect(Unit) {
        if (isInitialLaunch) {
            Log.d(TAG, "Showing splash screen for $SPLASH_DELAY ms")
            delay(SPLASH_DELAY)
        }

        Log.d(TAG, "Performing initial login check")
        val hasPlayerCharacter = loginManager.performInitialCheck()

        if (hasPlayerCharacter) {
            Log.d(TAG, "Player character exists, navigating to main menu")
            navigationManager.navigateToMainMenu()
        } else {
            Log.d(TAG, "No player character, navigating to welcome screen")
            navigationManager.navigateToWelcome()
        }

        Log.d(TAG, "Launch sequence completed")
        onFinish()
    }
}
