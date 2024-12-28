package com.github.cstrerath.uncover.ui.screens

import androidx.compose.runtime.*
import com.github.cstrerath.uncover.domain.auth.LoginManager
import com.github.cstrerath.uncover.ui.components.SplashScreen
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
        InitialLaunchContent(
            loginManager = loginManager,
            navigationManager = navigationManager,
            onFinish = onFinish
        )
    } else {
        MainMenuRedirect(
            navigationManager = navigationManager,
            onFinish = onFinish
        )
    }
}

@Composable
private fun InitialLaunchContent(
    loginManager: LoginManager,
    navigationManager: NavigationManager,
    onFinish: () -> Unit
) {
    SplashScreen()
    LaunchedEffect(Unit) {
        delay(2000)
        val hasPlayerCharacter = loginManager.performInitialCheck()

        if (hasPlayerCharacter) {
            navigationManager.navigateToMainMenu()
        } else {
            navigationManager.navigateToCharacterCreation()
        }

        onFinish()
    }
}

@Composable
private fun MainMenuRedirect(
    navigationManager: NavigationManager,
    onFinish: () -> Unit
) {
    LaunchedEffect(Unit) {
        navigationManager.navigateToMainMenu()
        onFinish()
    }
}