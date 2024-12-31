package com.github.cstrerath.uncover.ui.screens.achievement

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.ui.viewmodels.AchievementViewModel

private const val TAG = "AchievementScreen"

@Composable
internal fun AchievementScreen(viewModel: AchievementViewModel) {
    Log.d(TAG, "Initializing Achievement screen")
    val achievements by viewModel.achievements.collectAsState()

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        items(achievements) { achievement ->
            Log.d(TAG, "Rendering achievement: ${achievement.id}")
            AchievementItem(achievement)
        }
    }
}
