// ui/activities/MainMenuActivity.kt
package com.github.cstrerath.uncover.ui.activities

import androidx.compose.runtime.Composable
import com.github.cstrerath.uncover.ui.base.NoBackActivity
import com.github.cstrerath.uncover.ui.screens.mainmenu.MainMenuScreen

class MainMenuActivity : NoBackActivity() {
    @Composable
    override fun NoBackContent() {
        MainMenuScreen(
            navigationManager = navigationManager,
            questManager = questManager,
            randQuestManager = randQuestManager
        )
    }
}
