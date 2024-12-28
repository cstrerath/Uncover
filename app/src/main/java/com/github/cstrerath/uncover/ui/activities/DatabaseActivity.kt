package com.github.cstrerath.uncover.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.entities.CharacterClass
import com.github.cstrerath.uncover.data.database.entities.GameCharacter
import com.github.cstrerath.uncover.QuestViewModel
import com.github.cstrerath.uncover.data.database.entities.StepType
import com.github.cstrerath.uncover.ui.base.BaseActivity


class DatabaseActivity : BaseActivity() {
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