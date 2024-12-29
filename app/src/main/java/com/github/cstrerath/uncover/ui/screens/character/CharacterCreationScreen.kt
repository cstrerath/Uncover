package com.github.cstrerath.uncover.ui.screens.character

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.data.database.entities.CharacterClass

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
        CharacterCreationHeader()
        CharacterNameInput(
            characterName = characterName,
            onNameChange = { if (it.length <= 20) characterName = it }
        )
        CharacterClassSelection(
            selectedClass = selectedClass,
            onClassSelected = { selectedClass = it }
        )
        CreateCharacterButton(
            enabled = characterName.isNotBlank() && selectedClass != null,
            onClick = { onCharacterCreated(characterName, selectedClass) }
        )
    }
}