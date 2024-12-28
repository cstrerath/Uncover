package com.github.cstrerath.uncover.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun UncoverTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}