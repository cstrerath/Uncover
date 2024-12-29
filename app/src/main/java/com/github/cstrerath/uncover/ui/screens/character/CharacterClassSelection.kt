package com.github.cstrerath.uncover.ui.screens.character

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.data.database.entities.CharacterClass

@Composable
fun CharacterClassSelection(
    selectedClass: CharacterClass?,
    onClassSelected: (CharacterClass) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.choose_your_class),
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
