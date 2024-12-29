package com.github.cstrerath.uncover.data.database.initialization.data

import com.github.cstrerath.uncover.data.database.entities.Quest

object QuestData {
    private data class QuestLocations(
        val startLocationId: Int,
        val questLocationId: Int,
        val endLocationId: Int
    )

    private val questLocations = mapOf(
        1 to QuestLocations(1, 2, 1),
        2 to QuestLocations(1, 3, 1),
        3 to QuestLocations(1, 4, 1),
        4 to QuestLocations(1, 5, 1),
        5 to QuestLocations(1, 6, 1),
        6 to QuestLocations(1, 7, 1),
        7 to QuestLocations(1, 8, 1),
        8 to QuestLocations(1, 9, 1),
        9 to QuestLocations(1, 10, 1),
        10 to QuestLocations(1, 11, 1)
    )

    fun getAllQuests(): List<Quest> {
        return (1..10).map { questId ->
            createQuest(questId)
        }
    }

    private fun createQuest(questId: Int): Quest {
        val locations = questLocations[questId]
            ?: throw IllegalStateException("No locations defined for quest $questId")

        return Quest(
            questId = questId,
            questSequence = questId,
            resourceKey = "quest_$questId",
            startLocationId = locations.startLocationId,
            questLocationId = locations.questLocationId,
            endLocationId = locations.endLocationId
        )
    }
}