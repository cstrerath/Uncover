// ui/activities/MainActivity.kt
package com.github.cstrerath.uncover.ui.activities

import androidx.compose.runtime.Composable
import com.github.cstrerath.uncover.ui.base.NoBackActivity
import com.github.cstrerath.uncover.ui.screens.main.LaunchScreen

class MainActivity : NoBackActivity() {

    companion object {
        private var isInitialLaunch = true
    }

    @Composable
    override fun NoBackContent() {
        LaunchScreen(
            isInitialLaunch = isInitialLaunch,
            loginManager = loginManager,
            navigationManager = navigationManager,
            onFinish = {
                isInitialLaunch = false
                finish()
            }
        )
    }
}
