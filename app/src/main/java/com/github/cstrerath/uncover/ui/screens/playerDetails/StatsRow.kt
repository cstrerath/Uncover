package com.github.cstrerath.uncover.ui.screens.playerDetails

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.R

@Composable
internal fun StatsRow(mana: Int, health: Int, stamina: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatItem(
            stringResource(R.string.player_stats_mana),
            mana,
            if (isSystemInDarkTheme()) Color(0xFF8B9CFF) else Color(0xFF0000CC)
        )
        StatItem(
            stringResource(R.string.player_stats_health),
            health,
            if (isSystemInDarkTheme()) Color(0xFFFF8B8B) else Color(0xFFCC0000)
        )
        StatItem(
            stringResource(R.string.player_stats_stamina),
            stamina,
            if (isSystemInDarkTheme()) Color(0xFF90EE90) else Color(0xFF006600)
        )
    }
}
