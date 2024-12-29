package com.github.cstrerath.uncover.ui.screens.playerDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
internal fun StatsRow(mana: Int, health: Int, stamina: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem("Mana", mana, Color.Blue)
        StatItem("Health", health, Color.Red)
        StatItem("Stamina", stamina, Color.Green)
    }
}
