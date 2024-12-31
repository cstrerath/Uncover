package com.github.cstrerath.uncover.ui.screens.playerDetails

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

private const val TAG = "CharacterHeader"

@Composable
internal fun CharacterHeader(
    name: String,
    characterClass: String
) {
    Log.d(TAG, "Rendering character header for: $name ($characterClass)")

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CharacterName(name)
        CharacterClass(characterClass)
    }
}

@Composable
private fun CharacterName(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun CharacterClass(characterClass: String) {
    Text(
        text = characterClass,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    )
}
