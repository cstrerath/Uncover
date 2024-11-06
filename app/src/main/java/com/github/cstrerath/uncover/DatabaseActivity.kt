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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


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
fun CharacterItem(character: GameCharacter) {
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
        }
    }
}