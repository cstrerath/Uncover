package com.github.cstrerath.uncover.ui.screens.playerDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
            .padding(16.dp)
    ) {
        viewModel.player.value?.let { char ->
            CharacterHeader(char.name, char.characterClass.displayName)
            Spacer(modifier = Modifier.height(16.dp))
            StatsRow(char.mana, char.health, char.stamina)
            Spacer(modifier = Modifier.height(16.dp))
            ExperienceBar(
                experience = char.experience,
                requiredXp = characterProgression.getRequiredXpForNextLevel(char.level),
                level = char.level
            )
            Spacer(modifier = Modifier.height(8.dp))
            LevelDisplay(char.level)
        } ?: run {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Button(
            onClick = {
                scope.launch {
                    viewModel.tryLevelUp()
                }
            }
        ) {
            Text(stringResource(R.string.try_level_up_button))
        }

        Spacer(Modifier.padding(16.dp))

        Button(
            onClick = {
                scope.launch {
                    viewModel.addTestXp()
                }
            }
        ) {
            Text(stringResource(R.string.add_test_xp_button))
        }

        if (viewModel.dialogState.value.show) {
            LevelUpDialog(
                message = viewModel.dialogState.value.message,
                onDismiss = { viewModel.dismissDialog() }
            )
        }
    }
}