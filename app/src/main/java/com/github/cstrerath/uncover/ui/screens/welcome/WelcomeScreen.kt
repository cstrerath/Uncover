package com.github.cstrerath.uncover.ui.screens.welcome

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.R

private const val TAG = "WelcomeScreen"

@Composable
fun WelcomeScreen(
    onNavigateToNext: () -> Unit
) {
    var termsAccepted by remember { mutableStateOf(false) }
    Log.d(TAG, "Initializing Welcome screen")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WelcomeTitle()
        WelcomeCard(
            termsAccepted = termsAccepted,
            onTermsAcceptedChange = {
                Log.d(TAG, "Terms accepted changed to: $it")
                termsAccepted = it
            }
        )
        WelcomeAcceptTermsButton(
            enabled = termsAccepted,
            onClick = {
                Log.d(TAG, "Navigating to next screen")
                onNavigateToNext()
            }
        )
    }
}

@Composable
private fun WelcomeTitle() {
    Text(
        text = stringResource(R.string.welcome_title),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
private fun WelcomeCard(
    termsAccepted: Boolean,
    onTermsAcceptedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        WelcomeTextScreen(
            modifier = Modifier.padding(16.dp)
        )
        WelcomeTermsSelector(
            accepted = termsAccepted,
            onAcceptedChange = onTermsAcceptedChange
        )
    }
}
