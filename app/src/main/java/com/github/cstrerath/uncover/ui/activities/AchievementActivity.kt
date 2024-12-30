package com.github.cstrerath.uncover.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.Alignment
import androidx.lifecycle.ViewModelProvider
import com.github.cstrerath.uncover.ui.base.BaseActivity
import com.github.cstrerath.uncover.ui.screens.UncoverBaseScreen
import com.github.cstrerath.uncover.ui.screens.achievement.AchievementScreen
import com.github.cstrerath.uncover.ui.theme.UncoverTheme
import com.github.cstrerath.uncover.ui.viewmodels.AchievementViewModel


class AchievementActivity : BaseActivity() {
    private val viewModel: AchievementViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UncoverTheme {
                UncoverBaseScreen(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AchievementScreen(viewModel)
                }
            }
        }
    }
}
