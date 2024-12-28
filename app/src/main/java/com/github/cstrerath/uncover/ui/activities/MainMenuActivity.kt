// ui/activities/MainMenuActivity.kt
package com.github.cstrerath.uncover.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import com.github.cstrerath.uncover.ui.base.BaseActivity
import com.github.cstrerath.uncover.ui.screens.MainMenuScreen
import com.github.cstrerath.uncover.ui.theme.UncoverTheme

class MainMenuActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UncoverTheme {
                MainMenuScreen(
                    navigationManager = navigationManager,
                    questManager = questManager
                )
            }
        }
    }
}
