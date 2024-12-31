package com.github.cstrerath.uncover.ui.screens.playerDetails

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.data.database.entities.GameCharacter
import com.github.cstrerath.uncover.data.repository.CharacterRepository
import com.github.cstrerath.uncover.domain.character.progression.CharacterProgression
import com.github.cstrerath.uncover.ui.viewmodels.PlayerStatsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val TAG = "PlayerStatsScreen"

@Composable
fun PlayerStatsScreen() {
    Log.d(TAG, "Initializing Player Stats Screen")
    val context = LocalContext.current
    val characterProgression = CharacterProgression(context)  // Separate instance
    val scope = rememberCoroutineScope()
    val viewModel = remember {
        PlayerStatsViewModel(
            context,
            CharacterRepository(context),
            characterProgression
        )
    }

    LaunchedEffect(Unit) {
        Log.d(TAG, "Loading player data")
        viewModel.loadPlayer()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        viewModel.player.value?.let { char ->
            Log.d(TAG, "Rendering player stats for: ${char.name}")
            PlayerInfoCard(char)
            StatsCard(char)
            ExperienceCard(char, characterProgression)  // Use characterProgression instead of viewModel
            LevelUpButton(scope, viewModel)
        } ?: run {
            Log.d(TAG, "Player data not yet loaded, showing loading indicator")
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (viewModel.dialogState.value.show) {
            Log.d(TAG, "Showing level up dialog")
            LevelUpDialog(
                message = viewModel.dialogState.value.message,
                onDismiss = { viewModel.dismissDialog() }
            )
        }
    }
}

@Composable
private fun PlayerInfoCard(char: GameCharacter) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CharacterHeader(char.name, char.characterClass.displayName)
        }
    }
}

@Composable
private fun StatsCard(char: GameCharacter) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            StatsRow(char.mana, char.health, char.stamina)
        }
    }
}

@Composable
private fun ExperienceCard(
    char: GameCharacter,
    characterProgression: CharacterProgression
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ExperienceBar(
                experience = char.experience,
                requiredXp = characterProgression.getRequiredXpForNextLevel(char.level),
                level = char.level
            )
            Spacer(modifier = Modifier.height(8.dp))
            LevelDisplay(char.level)
        }
    }
}

@Composable
private fun LevelUpButton(
    scope: CoroutineScope,
    viewModel: PlayerStatsViewModel
) {
    val isDarkTheme = isSystemInDarkTheme()
    Button(
        onClick = {
            Log.d(TAG, "Level up button clicked")
            scope.launch {
                viewModel.tryLevelUp()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isDarkTheme)
                Color(0xFFB8A4DC)
            else
                MaterialTheme.colorScheme.primary
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp
        )
    ) {
        Text(
            text = stringResource(R.string.try_level_up_button),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (isDarkTheme)
                MaterialTheme.colorScheme.surface
            else
                MaterialTheme.colorScheme.onPrimary
        )
    }
}
