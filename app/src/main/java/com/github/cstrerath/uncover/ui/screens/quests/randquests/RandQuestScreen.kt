package com.github.cstrerath.uncover.ui.screens.quests.randquests

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.ui.screens.quests.ErrorMessage
import com.github.cstrerath.uncover.ui.screens.quests.LoadingIndicator
import com.github.cstrerath.uncover.ui.states.RandQuestUIState
import com.github.cstrerath.uncover.ui.viewmodels.RandQuestViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "RandQuestScreen"

@Composable
fun RandQuestScreen(
    locationId: Int,
    viewModel: RandQuestViewModel,
    onQuestComplete: () -> Unit
) {
    var randQuestState by remember { mutableStateOf<RandQuestUIState>(RandQuestUIState.Loading) }
    val coroutineScope = rememberCoroutineScope()

    Log.d(TAG, "Initializing RandQuest screen for location: $locationId")

    LaunchedEffect(locationId) {
        Log.d(TAG, "Loading quest info for location: $locationId")
        withContext(Dispatchers.IO) {
            randQuestState = viewModel.loadRandQuestInfo()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = randQuestState) {
            is RandQuestUIState.Loading -> {
                Log.d(TAG, "Showing loading state")
                LoadingIndicator()
            }
            is RandQuestUIState.QuestLoaded -> {
                Log.d(TAG, "Quest loaded for player: ${state.playerId}")
                RandQuestContent(
                    state = state,
                    onProgressQuest = {
                        Log.d(TAG, "Processing quest progress")
                        coroutineScope.launch {
                            viewModel.progressRandQuest(state.quest)
                            Log.d(TAG, "Quest completed, navigating away")
                            onQuestComplete()
                        }
                    }
                )
            }
            is RandQuestUIState.Error -> {
                Log.e(TAG, "Error state: ${state.message}")
                ErrorMessage(state.message)
            }
        }
    }
}
