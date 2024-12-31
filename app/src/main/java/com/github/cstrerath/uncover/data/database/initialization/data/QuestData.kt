package com.github.cstrerath.uncover.data.database.initialization.data

import com.github.cstrerath.uncover.data.database.entities.Quest

object QuestData {
    private data class QuestLocations(
        val startLocationId: Int,
        val questLocationId: Int,
        val endLocationId: Int
    )

    private val questLocations = mapOf(
        1 to QuestLocations(1, 2, 3),
        2 to QuestLocations(4, 5, 6),
        3 to QuestLocations(7, 8, 9),
        4 to QuestLocations(10, 11, 12),
        5 to QuestLocations(13, 14, 15),
        6 to QuestLocations(16, 17, 18),
        7 to QuestLocations(19, 20, 21),
        8 to QuestLocations(22, 23, 24),
        9 to QuestLocations(25, 26, 27),
        10 to QuestLocations(28, 29, 30)
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