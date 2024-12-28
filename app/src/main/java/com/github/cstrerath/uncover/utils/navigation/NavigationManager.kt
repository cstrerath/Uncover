// utils/navigation/NavigationManager.kt
package com.github.cstrerath.uncover.utils.navigation

import android.app.Activity
import android.content.Intent
import com.github.cstrerath.uncover.ui.activities.MapActivity
import com.github.cstrerath.uncover.ui.activities.DatabaseActivity
import com.github.cstrerath.uncover.ui.activities.CharacterCreationActivity
import com.github.cstrerath.uncover.ui.activities.LocationListActivity
import com.github.cstrerath.uncover.ui.activities.PlayerStatsActivity
import com.github.cstrerath.uncover.ui.activities.MainMenuActivity

// utils/navigation/NavigationManager.kt
class NavigationManager(private val activity: Activity) {
    fun navigateToMainMenu() {
        navigate(MainMenuActivity::class.java)
    }

    fun navigateToMap() {
        navigate(MapActivity::class.java)
    }

    fun navigateToCharacterList() {
        navigate(DatabaseActivity::class.java)
    }

    fun navigateToCharacterCreation() {
        navigate(CharacterCreationActivity::class.java)
    }

    fun navigateToLocations() {
        navigate(LocationListActivity::class.java)
    }

    fun navigateToPlayerStats() {
        navigate(PlayerStatsActivity::class.java)
    }

    private fun navigate(targetActivity: Class<*>) {
        activity.startActivity(Intent(activity, targetActivity))
    }
}

