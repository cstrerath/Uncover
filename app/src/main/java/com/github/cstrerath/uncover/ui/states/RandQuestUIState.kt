package com.github.cstrerath.uncover.ui.states

import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.data.database.entities.Location

sealed class RandQuestUIState {
    data object Loading : RandQuestUIState() {
        override fun toString() = "RandQuestUIState.Loading"
    }

    data class QuestLoaded(
        val questInfo: String,
        val quest: CharacterQuestProgress,
        val playerId: String,
        val location: Location
    ) : RandQuestUIState() {
        override fun toString(): String {
            return "RandQuestUIState.QuestLoaded(questId=${quest.questId}, playerId=$playerId)"
        }
    }

    data class Error(val message: String) : RandQuestUIState() {
        override fun toString() = "RandQuestUIState.Error(message=$message)"
    }
}
