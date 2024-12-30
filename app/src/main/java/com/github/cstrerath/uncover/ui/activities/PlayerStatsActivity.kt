package com.github.cstrerath.uncover.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import com.github.cstrerath.uncover.ui.base.BaseActivity
import com.github.cstrerath.uncover.ui.screens.UncoverBaseScreen
import com.github.cstrerath.uncover.ui.screens.playerDetails.PlayerStatsScreen
import com.github.cstrerath.uncover.ui.theme.UncoverTheme


class PlayerStatsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UncoverTheme {
                UncoverBaseScreen {
                    PlayerStatsScreen()
                }
            }
        }
    }
}
