package com.github.cstrerath.uncover.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import com.github.cstrerath.uncover.ui.base.BaseActivity
import com.github.cstrerath.uncover.ui.screens.UncoverBaseScreen
import com.github.cstrerath.uncover.ui.screens.playerDetails.PlayerStatsScreen
import com.github.cstrerath.uncover.ui.theme.UncoverTheme


class PlayerStatsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Creating Player Stats screen")
        setContent {
            UncoverTheme {
                UncoverBaseScreen(
                    content = { PlayerStatsScreen() }
                )
            }
        }
    }

    companion object {
        private const val TAG = "PlayerStatsActivity"
    }
}
