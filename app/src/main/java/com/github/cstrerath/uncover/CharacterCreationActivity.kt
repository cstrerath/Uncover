package com.github.cstrerath.uncover

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class CharacterCreationActivity : ComponentActivity() {
    private lateinit var characterCreator: PlayerCharacterCreator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getInstance(applicationContext)
        characterCreator = PlayerCharacterCreator(db.gameCharacterDao())

        setContent {
            UncoverTheme {
                CharacterCreationScreen(
                    onCharacterCreated = { name, characterClass ->
                        lifecycleScope.launch {
                            characterClass?.let {
                                characterCreator.createPlayerCharacter(name, it)
                                // Nach erfolgreicher Erstellung zur MainActivity
                                startActivity(Intent(this@CharacterCreationActivity, MainActivity::class.java))
                                finish()
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CharacterCreationScreen(
    onCharacterCreated: (name: String, characterClass: CharacterClass?) -> Unit
) {
    var characterName by remember { mutableStateOf("") }
    var selectedClass by remember { mutableStateOf<CharacterClass?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Erstelle deinen Charakter",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = characterName,
            onValueChange = { if (it.length <= 20) characterName = it },
            label = { Text("Charaktername") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Klassenauswahl
        CharacterClassSelection(
            selectedClass = selectedClass,
            onClassSelected = { selectedClass = it }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onCharacterCreated(characterName,selectedClass) },
            enabled = characterName.isNotBlank() && selectedClass != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Charakter erstellen")
        }
    }
}

@Composable
fun CharacterClassSelection(
    selectedClass: CharacterClass?,
    onClassSelected: (CharacterClass) -> Unit
) {
    Column {
        Text(
            text = "Wähle deine Klasse:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        CharacterClass.entries.forEach { characterClass ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = characterClass == selectedClass,
                        onClick = { onClassSelected(characterClass) }
                    )
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = characterClass == selectedClass,
                    onClick = { onClassSelected(characterClass) }
                )
                Text(
                    text = characterClass.displayName,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}