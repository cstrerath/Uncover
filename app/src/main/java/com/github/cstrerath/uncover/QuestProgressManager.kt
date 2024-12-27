package com.github.cstrerath.uncover

import android.util.Log
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuestProgressManager(
    private val progressDao: CharacterQuestProgressDao,
    private val questDao: QuestDao
    ) {
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

    /*suspend fun getActiveQuestLocations(characterId: String): List<Int> {
        return progressDao.getActiveQuestIds(characterId).map { questId ->
            val progress = progressDao.getQuestProgress(characterId, questId)
            when (progress?.stage) {
                QuestStage.AT_START -> questId // Start Location
                QuestStage.AT_QUEST_LOCATION -> questId + 1 // Quest Location
                QuestStage.AT_END -> questId + 2 // End Location
                else -> questId
            }
        }
    }*/

    suspend fun getActiveQuestLocations(characterId: String): List<Int> = withContext(Dispatchers.IO) {
        val listOfQuestLocations = ArrayList<Int>()
        val openProgressQuest = progressDao.getFirstIncompleteQuest(characterId)

        Log.i("Quest Progress:", openProgressQuest.toString())
        Log.i("Player Id:", characterId)

        if (openProgressQuest != null) {
            try {
                val openQuest = questDao.getQuestById(openProgressQuest.questId)
                Log.i("Quest:", openQuest.toString())

                when (openProgressQuest.stage) {
                    QuestStage.AT_START -> listOfQuestLocations.add(openQuest.startLocationId)
                    QuestStage.AT_QUEST_LOCATION -> listOfQuestLocations.add(openQuest.questLocationId)
                    QuestStage.AT_END -> listOfQuestLocations.add(openQuest.endLocationId)
                    // Für COMPLETED und NOT_STARTED wird nichts hinzugefügt
                    else -> { /* Nichts hinzufügen */ }
                }
            } catch (e: Exception) {
                Log.e("QuestMappingError:", "Fehler beim Abrufen der Quest: ${e.message}")
            }
        } else {
            Log.e("QuestMappingError:", "Keine offene Quest gefunden")
        }

        listOfQuestLocations
    }

}
