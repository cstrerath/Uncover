package com.github.cstrerath.uncover.ui.screens.quests

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private const val TAG = "LoadingIndicator"

@Composable
fun LoadingIndicator() {
    Log.d(TAG, "Showing loading indicator")
    CircularProgressIndicator(
        modifier = Modifier.size(48.dp),
        color = MaterialTheme.colorScheme.primary
    )
}
