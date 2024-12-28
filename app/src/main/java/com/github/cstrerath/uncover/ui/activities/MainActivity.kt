package com.github.cstrerath.uncover.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.github.cstrerath.uncover.domain.managers.LoginManager
import com.github.cstrerath.uncover.ui.components.SplashScreen
import com.github.cstrerath.uncover.ui.theme.UncoverTheme
import com.github.cstrerath.uncover.utils.navigation.NavigationManager
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private lateinit var navigationManager: NavigationManager
    private lateinit var loginManager: LoginManager

    companion object {
        private var isInitialLaunch = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeManagers()

        setContent {
            UncoverTheme {
                HandleAppLaunch()
            }
        }
    }

    private fun initializeManagers() {
        navigationManager = NavigationManager(this)
        loginManager = LoginManager(applicationContext)
    }

    @Composable
    private fun HandleAppLaunch() {
        if (isInitialLaunch) {
            SplashScreen()
            HandleInitialLaunch()
        } else {
            NavigateToMainMenu()
        }
    }

    @Composable
    private fun HandleInitialLaunch() {
        LaunchedEffect(Unit) {
            delay(2000)
            val hasPlayerCharacter = loginManager.performInitialCheck()

            if (hasPlayerCharacter) {
                navigationManager.navigateToMainMenu()
            } else {
                navigationManager.navigateToCharacterCreation()
            }

            isInitialLaunch = false
            finish()
        }
    }

    @Composable
    private fun NavigateToMainMenu() {
        LaunchedEffect(Unit) {
            navigationManager.navigateToMainMenu()
            finish()
        }
    }
}
