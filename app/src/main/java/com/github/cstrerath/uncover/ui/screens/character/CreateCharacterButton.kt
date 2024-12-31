package com.github.cstrerath.uncover.ui.screens.character

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.R

private const val TAG = "CreateCharacterButton"

@Composable
internal fun CreateCharacterButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Log.d(TAG, "Rendering create character button with enabled: $enabled")

    Button(
        onClick = {
            Log.d(TAG, "Create character button clicked")
            onClick()
        },
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text(
            text = stringResource(R.string.create_character),
            style = MaterialTheme.typography.titleMedium
        )
    }
}
