package com.github.cstrerath.uncover.ui.screens.welcome

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeScreen (
    onNavigateToNext : () -> Unit
) {
    var termsAccepted by remember { mutableStateOf(false)}

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            WelcomeTextScreen()

            WelcomeTermsSelector(
                accepted = termsAccepted,
                onAcceptedChange = { termsAccepted = it }
            )

            WelcomeAcceptTermsButton(
                enabled = termsAccepted,
                onClick = onNavigateToNext
            )
        }
    }
}