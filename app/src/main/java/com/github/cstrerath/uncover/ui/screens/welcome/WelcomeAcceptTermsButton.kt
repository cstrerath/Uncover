package com.github.cstrerath.uncover.ui.screens.welcome

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.R

private const val TAG = "WelcomeAcceptTermsBtn"

@Composable
internal fun WelcomeAcceptTermsButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Log.d(TAG, "Rendering accept terms button with enabled: $enabled")
    val isDarkTheme = isSystemInDarkTheme()

    Button(
        onClick = {
            Log.d(TAG, "Terms accepted button clicked")
            onClick()
        },
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isDarkTheme)
                MaterialTheme.colorScheme.surfaceVariant
            else
                MaterialTheme.colorScheme.primary,
            contentColor = if (isDarkTheme)
                MaterialTheme.colorScheme.onSurfaceVariant
            else
                MaterialTheme.colorScheme.onPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp
        )
    ) {
        Text(
            text = stringResource(R.string.continue_button),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
