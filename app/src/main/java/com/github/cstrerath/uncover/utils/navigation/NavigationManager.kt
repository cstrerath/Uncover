package com.github.cstrerath.uncover.utils.navigation

import android.app.Activity
import android.content.Intent
import com.github.cstrerath.uncover.ui.activities.CharacterCreationActivity
import com.github.cstrerath.uncover.ui.activities.MainMenuActivity

class NavigationManager(private val activity: Activity) {
    fun navigateToMainMenu() {
        navigate(MainMenuActivity::class.java)
    }

    fun navigateToCharacterCreation() {
        navigate(CharacterCreationActivity::class.java)
    }

    private fun navigate(targetActivity: Class<*>) {
        activity.startActivity(Intent(activity, targetActivity))
    }
}
