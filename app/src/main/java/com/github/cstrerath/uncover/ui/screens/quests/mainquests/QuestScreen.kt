// ui/screens/QuestScreen.kt
package com.github.cstrerath.uncover.ui.screens.quests.mainquests

import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.github.cstrerath.uncover.ui.screens.quests.ErrorMessage
import com.github.cstrerath.uncover.ui.screens.quests.LoadingIndicator
import com.github.cstrerath.uncover.ui.states.QuestUIState
import com.github.cstrerath.uncover.ui.viewmodels.QuestViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun QuestScreen(
    locationId: Int,
    viewModel: QuestViewModel,
    onQuestComplete: () -> Unit
) {
    var questState by remember { mutableStateOf<QuestUIState>(QuestUIState.Loading) }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    LaunchedEffect(locationId) {
        withContext(Dispatchers.IO) {
            questState = viewModel.loadQuestInfo()
        }
    }

    when (val state = questState) {
        is QuestUIState.Loading -> LoadingIndicator()
        is QuestUIState.QuestLoaded -> QuestContent(
            state = state,
            scrollState = scrollState,
            onProgressQuest = {
                coroutineScope.launch {
                    viewModel.progressQuest(state.quest)
                    onQuestComplete()
                }
            }
        )
        is QuestUIState.Error -> ErrorMessage(state.message)
    }
}