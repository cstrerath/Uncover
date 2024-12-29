package com.github.cstrerath.uncover.ui.states

import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.data.database.entities.Location

sealed class RandQuestUIState {
    data object Loading : RandQuestUIState()
    data class QuestLoaded(
        val questInfo: String,
        val quest: CharacterQuestProgress,
        val playerId: String,
        val location: Location
    ) : RandQuestUIState()
    data class Error(val message: String) : RandQuestUIState()
}
