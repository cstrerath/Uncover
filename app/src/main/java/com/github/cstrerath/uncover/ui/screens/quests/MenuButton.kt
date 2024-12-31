// ui/components/MenuButton.kt
package com.github.cstrerath.uncover.ui.screens.quests

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.domain.quest.mainquest.QuestManager
import com.github.cstrerath.uncover.domain.quest.randquest.RandQuestManager
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
        text = stringResource(R.string.quest_progress),
        onClick = {
            coroutineScope.launch {
                questManager.processNextQuest()
            }
        }
    )
}

@Composable
fun RandQuestProgressButton(randQuestManager: RandQuestManager) {
    val coroutineScope = rememberCoroutineScope()

    MenuButton(
        text = stringResource(R.string.rand_quest_progress),
        onClick = {
            coroutineScope.launch {
                randQuestManager.processNextRandQuest()
            }
        }
    )
}