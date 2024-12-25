package com.github.cstrerath.uncover

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