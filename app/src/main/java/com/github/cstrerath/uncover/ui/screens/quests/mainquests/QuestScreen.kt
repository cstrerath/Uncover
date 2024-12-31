// ui/screens/QuestScreen.kt
package com.github.cstrerath.uncover.ui.screens.quests.mainquests

import android.util.Log
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

private const val TAG = "QuestScreen"

@Composable
fun QuestScreen(
    locationId: Int,
    viewModel: QuestViewModel,
    onQuestComplete: () -> Unit
) {
    var questState by remember { mutableStateOf<QuestUIState>(QuestUIState.Loading) }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Log.d(TAG, "Initializing Quest screen for location: $locationId")

    LaunchedEffect(locationId) {
        Log.d(TAG, "Loading quest info for location: $locationId")
        withContext(Dispatchers.IO) {
            questState = viewModel.loadQuestInfo()
        }
    }

    when (val state = questState) {
        is QuestUIState.Loading -> {
            Log.d(TAG, "Showing loading state")
            LoadingIndicator()
        }
        is QuestUIState.QuestLoaded -> {
            Log.d(TAG, "Quest loaded: ${state.questTitle}")
            QuestContent(
                state = state,
                scrollState = scrollState,
                onProgressQuest = {
                    Log.d(TAG, "Processing quest progress")
                    coroutineScope.launch {
                        viewModel.progressQuest(state.quest)
                        Log.d(TAG, "Quest completed, navigating away")
                        onQuestComplete()
                    }
                }
            )
        }
        is QuestUIState.Error -> {
            Log.e(TAG, "Error state: ${state.message}")
            ErrorMessage(state.message)
        }
    }
}
