// ui/activities/MainMenuActivity.kt
package com.github.cstrerath.uncover.ui.activities

import android.util.Log
import androidx.compose.runtime.Composable
import com.github.cstrerath.uncover.ui.base.NoBackActivity
import com.github.cstrerath.uncover.ui.screens.mainmenu.MainMenuScreen

class MainMenuActivity : NoBackActivity() {
    @Composable
    override fun NoBackContent() {
        Log.d(TAG, "Displaying main menu")
        MainMenuScreen(
            navigationManager = navigationManager
        )
    }

    companion object {
        private const val TAG = "MainMenuActivity"
    }
}
