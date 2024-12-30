package com.github.cstrerath.uncover.ui.screens.quests.randquests

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.cstrerath.uncover.ui.screens.quests.ErrorMessage
import com.github.cstrerath.uncover.ui.screens.quests.LoadingIndicator
import com.github.cstrerath.uncover.ui.states.RandQuestUIState
import com.github.cstrerath.uncover.ui.viewmodels.RandQuestViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RandQuestScreen(
    locationId: Int,
    viewModel: RandQuestViewModel,
    onQuestComplete: () -> Unit
) {
    var randQuestState by remember { mutableStateOf<RandQuestUIState>(RandQuestUIState.Loading) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(locationId) {
        withContext(Dispatchers.IO) {
            randQuestState = viewModel.loadRandQuestInfo()
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = randQuestState) {
            is RandQuestUIState.Loading -> LoadingIndicator()
            is RandQuestUIState.QuestLoaded -> RandQuestContent(
                state = state,
                onProgressQuest = {
                    coroutineScope.launch {
                        viewModel.progressRandQuest(state.quest)
                        onQuestComplete()
                    }
                }
            )
            is RandQuestUIState.Error -> ErrorMessage(state.message)
        }
    }
}
