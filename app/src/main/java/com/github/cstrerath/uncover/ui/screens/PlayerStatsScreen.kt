package com.github.cstrerath.uncover.ui.screens

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.domain.character.progression.CharacterProgression
import com.github.cstrerath.uncover.data.database.entities.GameCharacter
import com.github.cstrerath.uncover.data.repository.CharacterRepository
import com.github.cstrerath.uncover.ui.base.BaseActivity
import com.github.cstrerath.uncover.ui.viewmodels.PlayerStatsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.withContext

@Composable
fun PlayerStatsScreen() {
    val context = LocalContext.current
    val characterProgression = CharacterProgression(context)
    val scope = rememberCoroutineScope()
    val viewModel = remember {
        PlayerStatsViewModel(
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
            Text("Level-Up-Try")
        }

        Spacer(Modifier.padding(16.dp))

        Button(
            onClick = {
                scope.launch {
                    viewModel.addTestXp()
                }
            }
        ) {
            Text("Add 250 XP")
        }

        if (viewModel.dialogState.value.show) {
            LevelUpDialog(
                message = viewModel.dialogState.value.message,
                onDismiss = { viewModel.dismissDialog() }
            )
        }
    }
}

@Composable
private fun LevelUpDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Level-Up Information") },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Composable
private fun CharacterHeader(name: String, characterClass: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = characterClass,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun StatsRow(mana: Int, health: Int, stamina: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem("Mana", mana, Color.Blue)
        StatItem("Health", health, Color.Red)
        StatItem("Stamina", stamina, Color.Green)
    }
}

@Composable
private fun StatItem(label: String, value: Int, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = color,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun ExperienceBar(experience: Int, requiredXp: Int, level: Int) {
    Column {
        if (level >= 25) {
            Text(
                text = "Maximallevel erreicht!",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            LinearProgressIndicator(
                progress = { 1f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        } else {
            val displayXp = experience.coerceAtMost(requiredXp)
            Text(
                text = "Experience: $displayXp / $requiredXp",
                style = MaterialTheme.typography.titleSmall
            )
            LinearProgressIndicator(
                progress = { (displayXp.toFloat() / requiredXp).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}

@Composable
private fun LevelDisplay(level: Int) {
    Text(
        text = "Level $level",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.primary
    )
}
