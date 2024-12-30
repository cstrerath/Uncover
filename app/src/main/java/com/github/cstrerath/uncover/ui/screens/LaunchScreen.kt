package com.github.cstrerath.uncover.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.github.cstrerath.uncover.domain.auth.LoginManager
import com.github.cstrerath.uncover.utils.navigation.NavigationManager
import kotlinx.coroutines.delay

@Composable
fun LaunchScreen(
    isInitialLaunch: Boolean,
    loginManager: LoginManager,
    navigationManager: NavigationManager,
    onFinish: () -> Unit
) {
    if (isInitialLaunch) {
        SplashScreen()
    }

    LaunchedEffect(Unit) {
        if (isInitialLaunch) {
            delay(2000)
        }

        val hasPlayerCharacter = loginManager.performInitialCheck()
        if (hasPlayerCharacter) {
            navigationManager.navigateToMainMenu()
        } else {
            navigationManager.navigateToWelcome()
        }

        onFinish()
    }
}
