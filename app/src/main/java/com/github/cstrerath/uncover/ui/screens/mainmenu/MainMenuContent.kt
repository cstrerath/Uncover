package com.github.cstrerath.uncover.ui.screens.mainmenu

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.utils.navigation.NavigationManager

private const val TAG = "MenuContent"
private val LARGE_TILE_HEIGHT = 200.dp
private val SMALL_TILE_HEIGHT = 150.dp

@Composable
fun MenuContent(navigationManager: NavigationManager) {
    Log.d(TAG, "Rendering menu content")

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        StartGameTile(navigationManager)
        Spacer(modifier = Modifier.height(16.dp))
        SecondaryTiles(navigationManager)
    }
}

@Composable
private fun StartGameTile(navigationManager: NavigationManager) {
    MenuTile(
        text = stringResource(R.string.start_game),
        onClick = {
            Log.d(TAG, "Navigating to map")
            navigationManager.navigateToMap()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(LARGE_TILE_HEIGHT)
    )
}

@Composable
private fun SecondaryTiles(navigationManager: NavigationManager) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MenuTile(
            text = stringResource(R.string.menu_player_stats),
            onClick = {
                Log.d(TAG, "Navigating to player stats")
                navigationManager.navigateToPlayerStats()
            },
            modifier = Modifier
                .weight(1f)
                .height(SMALL_TILE_HEIGHT)
        )

        MenuTile(
            text = stringResource(R.string.menu_achievements),
            onClick = {
                Log.d(TAG, "Navigating to achievements")
                navigationManager.navigateToAchievement()
            },
            modifier = Modifier
                .weight(1f)
                .height(SMALL_TILE_HEIGHT)
        )
    }
}

