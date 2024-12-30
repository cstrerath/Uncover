package com.github.cstrerath.uncover.ui.screens.quests

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ErrorMessage(message: String) {
    Text(
        text = message,
        color = Color.Red
    )
}