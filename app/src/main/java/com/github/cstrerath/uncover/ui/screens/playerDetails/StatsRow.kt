package com.github.cstrerath.uncover.ui.screens.playerDetails

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.R

private const val TAG = "StatsRow"

private object StatColors {
    object Mana {
        val Dark = Color(0xFF8B9CFF)
        val Light = Color(0xFF0000CC)
    }
    object Health {
        val Dark = Color(0xFFFF8B8B)
        val Light = Color(0xFFCC0000)
    }
    object Stamina {
        val Dark = Color(0xFF90EE90)
        val Light = Color(0xFF006600)
    }
}

@Composable
internal fun StatsRow(
    mana: Int,
    health: Int,
    stamina: Int
) {
    Log.d(TAG, "Rendering stats - Mana: $mana, Health: $health, Stamina: $stamina")
    val isDarkTheme = isSystemInDarkTheme()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatItem(
            label = stringResource(R.string.player_stats_mana),
            value = mana,
            color = if (isDarkTheme) StatColors.Mana.Dark else StatColors.Mana.Light
        )
        StatItem(
            label = stringResource(R.string.player_stats_health),
            value = health,
            color = if (isDarkTheme) StatColors.Health.Dark else StatColors.Health.Light
        )
        StatItem(
            label = stringResource(R.string.player_stats_stamina),
            value = stamina,
            color = if (isDarkTheme) StatColors.Stamina.Dark else StatColors.Stamina.Light
        )
    }
}
