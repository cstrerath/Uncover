package com.github.cstrerath.uncover.ui.states

import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress

sealed class QuestUIState {
    data object Loading : QuestUIState() {
        override fun toString() = "QuestUIState.Loading"
    }

    data class QuestLoaded(
        val questTitle: String,
        val questInfo: String,
        val quest: CharacterQuestProgress,
        val playerId: String
    ) : QuestUIState() {
        override fun toString(): String {
            return "QuestUIState.QuestLoaded(title=$questTitle, questId=${quest.questId}, playerId=$playerId)"
        }
    }

    data class Error(val message: String) : QuestUIState() {
        override fun toString() = "QuestUIState.Error(message=$message)"
    }
}
