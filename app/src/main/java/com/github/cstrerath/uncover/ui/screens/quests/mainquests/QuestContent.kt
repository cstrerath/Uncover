package com.github.cstrerath.uncover.ui.screens.quests.mainquests

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.data.database.entities.QuestStage
import com.github.cstrerath.uncover.ui.states.QuestUIState

private const val TAG = "QuestContent"

@Composable
fun QuestContent(
    state: QuestUIState.QuestLoaded,
    scrollState: ScrollState,
    onProgressQuest: () -> Unit
) {
    Log.d(TAG, "Displaying quest content for: ${state.questTitle}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        QuestHeader(state.questTitle)
        QuestInfoCard(state.questInfo)
        Spacer(modifier = Modifier.weight(1f))
        QuestProgress(state.quest.stage)
        QuestActionButton(
            stage = state.quest.stage,
            onProgressQuest = {
                Log.d(TAG, "Quest progress button clicked at stage: ${state.quest.stage}")
                onProgressQuest()
            }
        )
    }
}

@Composable
private fun QuestHeader(title: String) {
    Text(
        text = stringResource(R.string.main_quest_content_title),
        style = MaterialTheme.typography.displaySmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(bottom = 24.dp)
    )
}

@Composable
private fun QuestInfoCard(questInfo: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = questInfo,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun QuestProgress(stage: QuestStage) {
    Text(
        text = stringResource(R.string.main_quest_content_progress),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    LinearProgressIndicator(
        progress = {
            when (stage) {
                QuestStage.AT_START -> 0.33f
                QuestStage.AT_QUEST_LOCATION -> 0.66f
                QuestStage.AT_END -> 1f
                else -> 0f
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
            .clip(RoundedCornerShape(6.dp)),
        color = MaterialTheme.colorScheme.primary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant
    )

    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
private fun QuestActionButton(
    stage: QuestStage,
    onProgressQuest: () -> Unit
) {
    Button(
        onClick = onProgressQuest,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = when (stage) {
                QuestStage.AT_START -> stringResource(R.string.quest_accept_button)
                QuestStage.AT_QUEST_LOCATION -> stringResource(R.string.quest_action_button)
                QuestStage.AT_END -> stringResource(R.string.quest_finish_button)
                else -> stringResource(R.string.quest_unknown_button)
            },
            style = MaterialTheme.typography.labelLarge
        )
    }
}

