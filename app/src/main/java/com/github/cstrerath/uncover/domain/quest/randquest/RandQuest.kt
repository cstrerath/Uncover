package com.github.cstrerath.uncover.domain.quest.randquest

import com.github.cstrerath.uncover.data.database.entities.Location

/**
 * Represents a randomly generated quest with its location and description.
 *
 * @property id Unique identifier for the random quest
 * @property location The location where this quest takes place
 * @property textString The generated quest description text
 */
data class RandQuest(
    val id: Int,
    val location: Location,
    val textString: String
) {
    init {
        require(id >= MIN_QUEST_ID) { "Quest ID must be >= $MIN_QUEST_ID" }
        require(textString.isNotBlank()) { "Quest text cannot be empty" }
    }

    companion object {
        private const val MIN_QUEST_ID = 11 // Random quests start from ID 11
    }
}
