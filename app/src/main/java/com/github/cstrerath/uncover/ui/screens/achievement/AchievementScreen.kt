package com.github.cstrerath.uncover.ui.screens.achievement

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.github.cstrerath.uncover.ui.viewmodels.AchievementViewModel

@Composable
internal fun AchievementScreen(viewModel: AchievementViewModel) {
    val achievements by viewModel.achievements.collectAsState()

    LazyColumn {
        items(achievements) { achievement ->
            AchievementItem(achievement)
        }
    }
}