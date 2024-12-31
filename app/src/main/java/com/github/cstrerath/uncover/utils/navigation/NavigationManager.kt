// utils/navigation/NavigationManager.kt
package com.github.cstrerath.uncover.utils.navigation

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.github.cstrerath.uncover.ui.activities.AchievementActivity
import com.github.cstrerath.uncover.ui.activities.MainMenuActivity
import com.github.cstrerath.uncover.ui.activities.MapActivity
import com.github.cstrerath.uncover.ui.activities.PlayerStatsActivity
import com.github.cstrerath.uncover.ui.activities.WelcomeActivity

class NavigationManager(private val activity: Activity) {
    companion object {
        private const val TAG = "NavigationManager"
    }

    fun navigateToMainMenu() {
        Log.d(TAG, "Navigating to MainMenu")
        navigate(MainMenuActivity::class.java)
    }

    fun navigateToMap() {
        Log.d(TAG, "Navigating to Map")
        navigate(MapActivity::class.java)
    }

    fun navigateToPlayerStats() {
        Log.d(TAG, "Navigating to PlayerStats")
        navigate(PlayerStatsActivity::class.java)
    }

    fun navigateToWelcome() {
        Log.d(TAG, "Navigating to Welcome")
        navigate(WelcomeActivity::class.java)
    }

    fun navigateToAchievement() {
        Log.d(TAG, "Navigating to Achievement")
        navigate(AchievementActivity::class.java)
    }

    private fun navigate(targetActivity: Class<*>) {
        try {
            activity.startActivity(Intent(activity, targetActivity))
            Log.d(TAG, "Navigation successful to ${targetActivity.simpleName}")
        } catch (e: Exception) {
            Log.e(TAG, "Navigation failed to ${targetActivity.simpleName}", e)
        }
    }
}

