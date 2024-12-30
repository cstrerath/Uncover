// utils/navigation/NavigationManager.kt
package com.github.cstrerath.uncover.utils.navigation

import android.app.Activity
import android.content.Intent
import com.github.cstrerath.uncover.ui.activities.AchievementActivity
import com.github.cstrerath.uncover.ui.activities.MainMenuActivity
import com.github.cstrerath.uncover.ui.activities.MapActivity
import com.github.cstrerath.uncover.ui.activities.PlayerStatsActivity
import com.github.cstrerath.uncover.ui.activities.WelcomeActivity

// utils/navigation/NavigationManager.kt
class NavigationManager(private val activity: Activity) {
    fun navigateToMainMenu() {
        navigate(MainMenuActivity::class.java)
    }

    fun navigateToMap() {
        navigate(MapActivity::class.java)
    }

    fun navigateToPlayerStats() {
        navigate(PlayerStatsActivity::class.java)
    }

    fun navigateToWelcome() {
        navigate(WelcomeActivity::class.java)
    }

    fun navigateToAchievement() {
        navigate(AchievementActivity::class.java)
    }

    private fun navigate(targetActivity: Class<*>) {
        activity.startActivity(Intent(activity, targetActivity))
    }

}

