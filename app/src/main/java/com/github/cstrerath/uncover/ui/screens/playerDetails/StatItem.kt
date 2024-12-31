package com.github.cstrerath.uncover.ui.screens.playerDetails

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private const val TAG = "StatItem"
private const val MAX_STAT_VALUE = 600f
private val LABEL_WIDTH = 80.dp
private val VALUE_WIDTH = 60.dp
private val BAR_HEIGHT = 40.dp
private const val DARK_MODE_ALPHA = 0.8f
private const val LIGHT_MODE_ALPHA = 1f

@Composable
internal fun StatItem(
    label: String,
    value: Int,
    color: Color
) {
    Log.d(TAG, "Rendering stat item: $label = $value")
    val isDarkTheme = isSystemInDarkTheme()
    val colorAlpha = if (isDarkTheme) DARK_MODE_ALPHA else LIGHT_MODE_ALPHA

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatLabel(label)
        Box(modifier = Modifier.weight(1f)) {
            StatBar(value, color, colorAlpha)
        }
        StatValue(value, color, colorAlpha)
    }

}

@Composable
private fun StatLabel(label: String) {
    Text(
        text = label,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.width(LABEL_WIDTH)
    )
}

@Composable
private fun StatBar(
    value: Int,
    color: Color,
    alpha: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(value.toFloat() / MAX_STAT_VALUE)
            .height(BAR_HEIGHT),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = alpha)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) { }
}


@Composable
private fun StatValue(value: Int, color: Color, alpha: Float) {
    Text(
        text = value.toString(),
        style = MaterialTheme.typography.headlineMedium,
        color = color.copy(alpha = alpha),
        fontWeight = FontWeight.Bold,
        modifier = Modifier.width(VALUE_WIDTH)
    )
}
