package com.github.cstrerath.uncover

class QuestProgressManager(private val progressDao: CharacterQuestProgressDao) {
    suspend fun progressQuest(characterId: String, questId: Int) {
        val currentProgress = progressDao.getQuestProgress(characterId, questId)
            ?: CharacterQuestProgress(characterId, questId, QuestStage.NOT_STARTED)

        val newStage = when {
            // Erste Quest starten
            questId == 1 && currentProgress.stage == QuestStage.NOT_STARTED ->
                QuestStage.AT_START

            // Quest fortschreiten
            currentProgress.stage < QuestStage.AT_END ->
                QuestStage.entries[currentProgress.stage.ordinal + 1]

            // Letzte Quest abschließen
            questId == 10 && currentProgress.stage == QuestStage.AT_END ->
                QuestStage.COMPLETED

            // Nächste Quest starten
            currentProgress.stage == QuestStage.AT_END -> {
                // Aktuelle Quest als abgeschlossen markieren
                progressDao.updateProgress(currentProgress.copy(stage = QuestStage.COMPLETED))
                // Nächste Quest initialisieren
                val nextQuestId = questId + 1
                progressDao.updateProgress(CharacterQuestProgress(
                    characterId = characterId,
                    questId = nextQuestId,
                    stage = QuestStage.AT_START
                ))
                return
            }
            else -> currentProgress.stage
        }

        progressDao.updateProgress(currentProgress.copy(stage = newStage))
    }

    suspend fun getActiveQuestLocations(characterId: String): List<Int> {
        return progressDao.getActiveQuestIds(characterId).map { questId ->
            val progress = progressDao.getQuestProgress(characterId, questId)
            when (progress?.stage) {
                QuestStage.AT_START -> questId // Start Location
                QuestStage.AT_QUEST_LOCATION -> questId + 1 // Quest Location
                QuestStage.AT_END -> questId + 2 // End Location
                else -> questId
            }
        }
    }
}
