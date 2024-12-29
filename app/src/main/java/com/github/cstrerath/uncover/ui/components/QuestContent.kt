package com.github.cstrerath.uncover.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.data.database.entities.QuestStage
import com.github.cstrerath.uncover.ui.states.QuestUIState

@Composable
fun QuestContent(
    state: QuestUIState.QuestLoaded,
    onProgressQuest: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = state.questInfo,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onProgressQuest
        ) {
            Text(
                text = when (state.quest.stage) {
                    QuestStage.AT_START -> stringResource(R.string.quest_accept_button)
                    QuestStage.AT_QUEST_LOCATION -> stringResource(R.string.quest_action_button)
                    QuestStage.AT_END -> stringResource(R.string.quest_finish_button)
                    else -> stringResource(R.string.quest_unknown_button)
                }
            )
        }
    }
}