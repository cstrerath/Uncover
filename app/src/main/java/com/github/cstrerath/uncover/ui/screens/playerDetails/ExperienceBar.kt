package com.github.cstrerath.uncover.ui.screens.playerDetails

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.R

private const val TAG = "ExperienceBar"
private const val MAX_LEVEL = 25

@Composable
internal fun ExperienceBar(
    experience: Int,
    requiredXp: Int,
    level: Int
) {
    Log.d(TAG, "Rendering experience bar - Level: $level, XP: $experience/$requiredXp")

    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        if (level >= MAX_LEVEL) {
            MaxLevelContent()
        } else {
            ExperienceContent(experience, requiredXp)
        }
    }
}

@Composable
private fun MaxLevelContent() {
    Text(
        text = stringResource(R.string.max_level_reached),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold
    )
    ProgressBar(progress = 1f, color = MaterialTheme.colorScheme.primary)
}

@Composable
private fun ExperienceContent(experience: Int, requiredXp: Int) {
    val displayXp = experience.coerceAtMost(requiredXp)
    val progress = (displayXp.toFloat() / requiredXp).coerceIn(0f, 1f)

    Text(
        text = "Experience: $displayXp / $requiredXp",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    ProgressBar(progress = progress, color = MaterialTheme.colorScheme.secondary)
}

@Composable
private fun ProgressBar(progress: Float, color: Color) {
    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
            .clip(RoundedCornerShape(8.dp)),
        color = color,
        trackColor = MaterialTheme.colorScheme.surface,
    )
}
