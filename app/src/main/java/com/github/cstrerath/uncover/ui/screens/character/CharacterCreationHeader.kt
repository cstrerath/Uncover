package com.github.cstrerath.uncover.ui.screens.character


import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.R

private const val TAG = "CharacterCreationHeader"

@Composable
internal fun CharacterCreationHeader() {
    Log.d(TAG, "Rendering character creation header")

    Text(
        text = stringResource(R.string.create_your_character),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 32.dp)
    )
}
