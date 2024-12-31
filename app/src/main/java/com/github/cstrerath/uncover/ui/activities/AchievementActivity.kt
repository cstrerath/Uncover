package com.github.cstrerath.uncover.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.github.cstrerath.uncover.ui.base.BaseActivity
import com.github.cstrerath.uncover.ui.screens.UncoverBaseScreen
import com.github.cstrerath.uncover.ui.screens.achievement.AchievementScreen
import com.github.cstrerath.uncover.ui.theme.UncoverTheme
import com.github.cstrerath.uncover.ui.viewmodels.AchievementViewModel


class AchievementActivity : BaseActivity() {
    private val viewModel by viewModels<AchievementViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Creating Achievement screen")
        setContent {
            UncoverTheme {
                UncoverBaseScreen(
                    content = { AchievementScreen(viewModel) }
                )
            }
        }
    }

    companion object {
        private const val TAG = "AchievementActivity"
    }
}
