// ui/components/MenuButton.kt
package com.github.cstrerath.uncover.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.github.cstrerath.uncover.domain.managers.QuestManager
import kotlinx.coroutines.launch

@Composable
fun MenuButton(
    text: String,
    onClick: () -> Unit
) {
    Button(onClick = onClick) {
        Text(text)
    }
}

@Composable
fun QuestProgressButton(questManager: QuestManager) {
    val coroutineScope = rememberCoroutineScope()

    MenuButton(
        text = "Quest-Progress",
        onClick = {
            coroutineScope.launch {
                questManager.processNextQuest()
            }
        }
    )
}
