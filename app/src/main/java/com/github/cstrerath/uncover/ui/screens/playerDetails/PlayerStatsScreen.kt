package com.github.cstrerath.uncover.ui.screens.playerDetails

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
import com.github.cstrerath.uncover.data.repository.CharacterRepository
import com.github.cstrerath.uncover.domain.character.progression.CharacterProgression
import com.github.cstrerath.uncover.ui.viewmodels.PlayerStatsViewModel
import kotlinx.coroutines.launch

@Composable
fun PlayerStatsScreen() {
    val context = LocalContext.current
    val characterProgression = CharacterProgression(context)
    val scope = rememberCoroutineScope()
    val viewModel = remember {
        PlayerStatsViewModel(
            context,
            CharacterRepository(context),
            CharacterProgression(context)
        )
    }

    LaunchedEffect(Unit) {
        viewModel.loadPlayer()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        viewModel.player.value?.let { char ->
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
                    modifier = Modifier.padding(16.dp)
                ) {
                    StatsRow(char.mana, char.health, char.stamina)
                }
            }

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
                    modifier = Modifier.padding(16.dp)
                ) {
                    ExperienceBar(
                        experience = char.experience,
                        requiredXp = characterProgression.getRequiredXpForNextLevel(char.level),
                        level = char.level
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LevelDisplay(char.level)
                }
            }

            Button(
                onClick = {
                    scope.launch {
                        viewModel.tryLevelUp()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSystemInDarkTheme())
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
                    color = if (isSystemInDarkTheme())
                        MaterialTheme.colorScheme.surface
                    else
                        MaterialTheme.colorScheme.onPrimary
                )
            }


        } ?: run {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (viewModel.dialogState.value.show) {
            LevelUpDialog(
                message = viewModel.dialogState.value.message,
                onDismiss = { viewModel.dismissDialog() }
            )
        }
    }
}