package com.github.cstrerath.uncover.ui.screens.character

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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            CharacterCreationHeader()

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    CharacterNameInput(
                        characterName = characterName,
                        onNameChange = { if (it.length <= 20) characterName = it }
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    CharacterClassSelection(
                        selectedClass = selectedClass,
                        onClassSelected = { selectedClass = it }
                    )
                }
            }

            CreateCharacterButton(
                enabled = characterName.isNotBlank() && selectedClass != null,
                onClick = { onCharacterCreated(characterName, selectedClass) }
            )
        }
    }
}