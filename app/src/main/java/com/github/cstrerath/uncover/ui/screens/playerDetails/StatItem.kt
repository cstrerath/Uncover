package com.github.cstrerath.uncover.ui.screens.playerDetails

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

@Composable
internal fun StatItem(label: String, value: Int, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.width(80.dp)
        )

        Box(modifier = Modifier.weight(1f)) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(value.toFloat() / 600f)
                    .height(40.dp),
                colors = CardDefaults.cardColors(
                    containerColor = color.copy(alpha = if (isSystemInDarkTheme()) 0.8f else 1f)
                ),
                elevation = CardDefaults.cardElevation(6.dp)
            ) { }
        }

        Text(
            text = value.toString(),
            style = MaterialTheme.typography.headlineMedium,
            color = color.copy(alpha = if (isSystemInDarkTheme()) 0.8f else 1f),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(60.dp)
        )
    }
}