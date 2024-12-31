package com.github.cstrerath.uncover.ui.screens.achievement

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.data.database.entities.Achievement

private const val TAG = "AchievementItem"

@Composable
internal fun AchievementItem(
    achievement: Achievement,
    modifier: Modifier = Modifier
) {
    Log.d(TAG, "Rendering achievement item: ${achievement.stringResourceId}, reached: ${achievement.reached}")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        AchievementContent(achievement)
    }
}

@Composable
private fun AchievementContent(achievement: Achievement) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AchievementIcon(achievement)
        AchievementText(achievement)
    }
}

@Composable
private fun AchievementIcon(achievement: Achievement) {
    Image(
        painter = painterResource(
            id = if (achievement.reached) {
                R.drawable.achievement_unlocked
            } else {
                R.drawable.achievement_locked
            }
        ),
        contentDescription = stringResource(
            id = achievement.stringResourceId
        ),
        modifier = Modifier.size(72.dp)
    )
}

@Composable
private fun AchievementText(achievement: Achievement) {
    Text(
        text = stringResource(id = achievement.stringResourceId),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 12.dp)
    )
}
