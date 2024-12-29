package com.github.cstrerath.uncover.ui.screens.character


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.R

@Composable
internal fun CharacterNameInput(
    characterName: String,
    onNameChange: (String) -> Unit
) {
    OutlinedTextField(
        value = characterName,
        onValueChange = onNameChange,
        label = { Text(stringResource(R.string.character_name)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(24.dp))
}