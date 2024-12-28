package com.github.cstrerath.uncover.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    QuestStage.AT_START -> "Ich nehme die Herausforderung an!"
                    QuestStage.AT_QUEST_LOCATION -> "Ich stelle mich der Aufgabe!"
                    QuestStage.AT_END -> "Mission erfolgreich abgeschlossen!"
                    else -> "Weiter"
                }
            )
        }
    }
}