package com.github.cstrerath.uncover.ui.states

import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress

sealed class QuestUIState {
    data object Loading : QuestUIState()
    data class QuestLoaded(
        val questInfo: String,
        val quest: CharacterQuestProgress,
        val playerId: String
    ) : QuestUIState()
    data class Error(val message: String) : QuestUIState()
}
