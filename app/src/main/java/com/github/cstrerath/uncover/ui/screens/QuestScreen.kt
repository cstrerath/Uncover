// ui/screens/QuestScreen.kt
package com.github.cstrerath.uncover.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.cstrerath.uncover.ui.components.LoadingIndicator
import com.github.cstrerath.uncover.ui.components.ErrorMessage
import com.github.cstrerath.uncover.ui.components.QuestContent
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

    LaunchedEffect(locationId) {
        withContext(Dispatchers.IO) {
            questState = viewModel.loadQuestInfo()
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = questState) {
            is QuestUIState.Loading -> LoadingIndicator()
            is QuestUIState.QuestLoaded -> QuestContent(
                state = state,
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
}
