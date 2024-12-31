package com.github.cstrerath.uncover.ui.screens.character

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.data.database.entities.CharacterClass

private const val TAG = "CharacterCreationScreen"
private const val MAX_NAME_LENGTH = 20

@Composable
fun CharacterCreationScreen(
    initialName: String? = null,
    initialClass: CharacterClass? = null,
    onCharacterCreated: (String, CharacterClass?) -> Unit,
    onValuesChanged: (String, CharacterClass?) -> Unit
) {
    var characterName by remember { mutableStateOf(initialName ?: "") }
    var selectedClass by remember { mutableStateOf(initialClass) }

    Log.d(TAG, "Initializing character creation screen with initial name: $initialName, initial class: $initialClass")

    LaunchedEffect(characterName, selectedClass) {
        Log.d(TAG, "Values changed - updating Activity state - name: $characterName, class: $selectedClass")
        onValuesChanged(characterName, selectedClass)
    }

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            CharacterCreationContent(
                characterName = characterName,
                selectedClass = selectedClass,
                onNameChange = { newName ->
                    if (newName.length <= MAX_NAME_LENGTH) {
                        Log.d(TAG, "Character name changed to: $newName")
                        characterName = newName
                    }
                },
                onClassSelected = { newClass ->
                    Log.d(TAG, "Character class selected: $newClass")
                    selectedClass = newClass
                },
                onCharacterCreated = {
                    Log.d(TAG, "Creating character: $characterName with class: $selectedClass")
                    onCharacterCreated(characterName, selectedClass)
                }
            )
        }
    }
}

@Composable
private fun CharacterCreationContent(
    characterName: String,
    selectedClass: CharacterClass?,
    onNameChange: (String) -> Unit,
    onClassSelected: (CharacterClass) -> Unit,
    onCharacterCreated: () -> Unit
) {
    CharacterCreationHeader()

    CharacterInputCard(
        characterName = characterName,
        onNameChange = onNameChange
    )

    ClassSelectionCard(
        selectedClass = selectedClass,
        onClassSelected = onClassSelected
    )

    CreateCharacterButton(
        enabled = characterName.isNotBlank() && selectedClass != null,
        onClick = onCharacterCreated
    )
}

@Composable
private fun CharacterInputCard(
    characterName: String,
    onNameChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            CharacterNameInput(
                characterName = characterName,
                onNameChange = onNameChange
            )
        }
    }
}

@Composable
private fun ClassSelectionCard(
    selectedClass: CharacterClass?,
    onClassSelected: (CharacterClass) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            CharacterClassSelection(
                selectedClass = selectedClass,
                onClassSelected = onClassSelected
            )
        }
    }
}
