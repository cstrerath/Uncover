package com.github.cstrerath.uncover.ui.activities

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
import com.github.cstrerath.uncover.AppDatabase
import com.github.cstrerath.uncover.CharacterProgression
import com.github.cstrerath.uncover.GameCharacter
import com.github.cstrerath.uncover.ui.base.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PlayerStatsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlayerStatsScreen()
        }
    }
}

@Composable
fun PlayerStatsScreen() {
    val context = LocalContext.current
    var player by remember {
        mutableStateOf<GameCharacter?>(null)
    }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    val characterProgression = CharacterProgression(context)
    val scope = rememberCoroutineScope()

    // Funktion zum Neuladen des Spielers
    fun refreshPlayer() {
        scope.launch {
            withContext(Dispatchers.IO) {
                val database = AppDatabase.getInstance(context)
                val gameCharDao = database.gameCharacterDao()
                player = gameCharDao.getPlayerCharacter()
            }
        }
    }

    // Initial Load
    LaunchedEffect(Unit) {
        refreshPlayer()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        player?.let { char ->
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

        Button(onClick = {
            player?.let { char ->
                val remainingXp = characterProgression.getRemainingXp(char.level, char.experience)
                if (remainingXp <= 0) {
                    dialogMessage = "Levelaufstieg erfolgreich! Du bist nun Level ${char.level + 1}"
                } else {
                    dialogMessage = "Levelaufstieg nicht möglich! Dir fehlen noch $remainingXp XP bis zum nächsten Level."
                }
                showDialog = true
                characterProgression.tryLevelUp()
                // UI nach Level-Up aktualisieren
                scope.launch {
                    delay(100) // Kleine Verzögerung für die Datenbankoperation
                    refreshPlayer()
                }
            }
        }) {
            Text("Level-Up-Try")
        }

        Spacer(Modifier.padding(16.dp))

        Button(onClick = {
            characterProgression.addTestXp()
            dialogMessage = "250 XP hinzugefügt!"
            showDialog = true
            // UI nach XP-Hinzufügung aktualisieren
            scope.launch {
                delay(100) // Kleine Verzögerung für die Datenbankoperation
                refreshPlayer()
            }
        }) {
            Text("Add 250 XP")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Level-Up Information") },
                text = { Text(dialogMessage) },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
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
