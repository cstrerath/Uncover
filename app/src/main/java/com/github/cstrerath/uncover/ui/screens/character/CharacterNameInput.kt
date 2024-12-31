package com.github.cstrerath.uncover.ui.screens.character


import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.R

private const val TAG = "CharacterNameInput"

@Composable
internal fun CharacterNameInput(
    characterName: String,
    onNameChange: (String) -> Unit
) {
    Log.d(TAG, "Rendering name input with current name: $characterName")

    InputHeader()
    NameTextField(
        characterName = characterName,
        onNameChange = { newName ->
            Log.d(TAG, "Name changed to: $newName")
            onNameChange(newName)
        }
    )
}

@Composable
private fun InputHeader() {
    Text(
        text = stringResource(R.string.enter_character_name),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun NameTextField(
    characterName: String,
    onNameChange: (String) -> Unit
) {
    OutlinedTextField(
        value = characterName,
        onValueChange = onNameChange,
        label = { Text(stringResource(R.string.character_name)) },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
