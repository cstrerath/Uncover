package com.github.cstrerath.uncover

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel




class DatabaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CharacterListScreen()
        }
    }
}


@Composable
fun CharacterListScreen() {
    val context = LocalContext.current
    var characters by remember { mutableStateOf(emptyList<GameCharacter>()) }

    LaunchedEffect(Unit) {
        val database = AppDatabase.getInstance(context.applicationContext)
        val characterDao = database.gameCharacterDao()
        characters = characterDao.getAllCharacters()
    }

    LazyColumn {
        items(characters) { character ->
            CharacterItem(character)
        }
    }
}

@Composable
fun CharacterItem(character: GameCharacter, viewModel: QuestViewModel = viewModel()) {
    var showQuests by remember { mutableStateOf(false) }
    val quests by viewModel.quests.collectAsState()
    val questSteps by viewModel.questSteps.collectAsState()
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = character.name, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Class: ${character.characterClass}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Level: ${character.level}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Health: ${character.health}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Mana: ${character.mana}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Stamina: ${character.stamina}", style = MaterialTheme.typography.bodyMedium)

            Button(
                onClick = {
                    showQuests = !showQuests
                    if (showQuests) {
                        viewModel.loadQuestsForCharacter(1) // Lädt Quest 1
                    }
                }
            ) {
                Text(if (showQuests) "Hide Quests" else "Show Quests")
            }

            if (showQuests) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Available Quests:",
                    style = MaterialTheme.typography.titleMedium
                )
                quests.forEach { quest ->
                    val relevantStep = questSteps.find { it.questId == quest.questId && it.stepType == StepType.INITIAL }
                    val questText = when(character.characterClass) {
                        CharacterClass.WARRIOR -> relevantStep?.warriorVariantKey
                        CharacterClass.THIEF -> relevantStep?.thiefVariantKey
                        CharacterClass.MAGE -> relevantStep?.mageVariantKey
                    }
                    if (questText != null) {
                        Text(
                            text = context.getString(context.resources.getIdentifier(questText, "string", context.packageName)),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}