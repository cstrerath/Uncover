// ui/activities/MainActivity.kt
package com.github.cstrerath.uncover.ui.activities

import android.util.Log
import androidx.compose.runtime.Composable
import com.github.cstrerath.uncover.ui.base.NoBackActivity
import com.github.cstrerath.uncover.ui.screens.main.LaunchScreen

class MainActivity : NoBackActivity() {
    @Composable
    override fun NoBackContent() {
        Log.d(TAG, "Launching main screen with initial launch: $isInitialLaunch")
        LaunchScreen(
            isInitialLaunch = isInitialLaunch,
            loginManager = loginManager,
            navigationManager = navigationManager,
            onFinish = ::handleFinish
        )
    }

    private fun handleFinish() {
        Log.d(TAG, "Finishing initial launch sequence")
        isInitialLaunch = false
        finish()
    }

    companion object {
        private const val TAG = "MainActivity"
        private var isInitialLaunch = true
    }
}
