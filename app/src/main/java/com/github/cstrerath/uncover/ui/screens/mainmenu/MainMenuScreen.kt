// ui/screens/MainMenuScreen.kt
package com.github.cstrerath.uncover.ui.screens.mainmenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.github.cstrerath.uncover.BuildConfig
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.utils.navigation.NavigationManager

@Composable
fun MainMenuScreen(
    navigationManager: NavigationManager,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Uncover",
            style = MaterialTheme.typography.displayLarge.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = null,
            modifier = Modifier
                .size(240.dp)
                .padding(vertical = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        MenuContent(navigationManager)

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "v${BuildConfig.VERSION_NAME}",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MenuContent(navigationManager: NavigationManager) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MenuTile(
            text = stringResource(R.string.start_game),
            onClick = { navigationManager.navigateToMap() },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MenuTile(
                text = stringResource(R.string.menu_player_stats),
                onClick = { navigationManager.navigateToPlayerStats() },
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp)
            )

            MenuTile(
                text = stringResource(R.string.menu_achievements),
                onClick = { navigationManager.navigateToAchievement() },
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp)
            )
        }
    }
}

@Composable
fun MenuTile(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
