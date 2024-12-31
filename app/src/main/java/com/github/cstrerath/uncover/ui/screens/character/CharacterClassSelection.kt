package com.github.cstrerath.uncover.ui.screens.character

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.data.database.entities.CharacterClass

private const val TAG = "CharacterClassSelection"

@Composable
internal fun CharacterClassSelection(
    selectedClass: CharacterClass?,
    onClassSelected: (CharacterClass) -> Unit
) {
    Log.d(TAG, "Rendering class selection with selected class: $selectedClass")

    SelectionHeader()
    ClassSelectionList(
        selectedClass = selectedClass,
        onClassSelected = { newClass ->
            Log.d(TAG, "Class selected: $newClass")
            onClassSelected(newClass)
        }
    )
}

@Composable
private fun SelectionHeader() {
    Text(
        text = stringResource(R.string.choose_your_class),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun ClassSelectionList(
    selectedClass: CharacterClass?,
    onClassSelected: (CharacterClass) -> Unit
) {
    CharacterClass.entries.forEach { characterClass ->
        ClassSelectionRow(
            characterClass = characterClass,
            isSelected = characterClass == selectedClass,
            onSelected = onClassSelected
        )
    }
}

@Composable
private fun ClassSelectionRow(
    characterClass: CharacterClass,
    isSelected: Boolean,
    onSelected: (CharacterClass) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = { onSelected(characterClass) }
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = { onSelected(characterClass) },
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            text = characterClass.displayName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
