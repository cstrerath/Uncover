package com.github.cstrerath.uncover.domain.achievement

import android.content.Context
import android.util.Log
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.data.database.entities.GameCharacter
import com.github.cstrerath.uncover.data.database.entities.QuestStage

class AchievementManager(context: Context) {
    private val tag = "AchievementManager"
    private val database = AppDatabase.getInstance(context)
    private val achievementDao = database.achievementDao()
    private val questProgressDao = database.characterQuestProgressDao()
    private val gameCharacterDao = database.gameCharacterDao()

    suspend fun checkAndUpdateAchievements() {
        Log.d(tag, "Starting achievement check")
        try {
            val playerChar = gameCharacterDao.getPlayerCharacter() ?: run {
                Log.w(tag, "No player character found")
                return
            }

            val questProgress = questProgressDao.getCharacterProgress(playerChar.id)
            Log.d(tag, "Found ${questProgress.size} quest progress entries")

            checkMainQuestAchievements(questProgress)
            checkSideQuestAchievements(questProgress)
            checkLevelAchievements(playerChar)
            checkExperienceAchievements(playerChar)
            checkLegendAchievement()

            Log.d(tag, "Achievement check completed")
        } catch (e: Exception) {
            Log.e(tag, "Error during achievement check: ${e.message}")
            throw e
        }
    }

    private suspend fun checkMainQuestAchievements(progress: List<CharacterQuestProgress>) {
        Log.d(tag, "Checking main quest achievements")
        for (questId in 1..MAIN_QUEST_COUNT) {
            val completed = progress.any { it.questId == questId && it.stage == QuestStage.COMPLETED }
            if (completed) {
                Log.d(tag, "Main quest $questId completed, unlocking achievement")
                achievementDao.updateAchievementStatus(questId, true)
            }
        }
    }

    private suspend fun checkSideQuestAchievements(progress: List<CharacterQuestProgress>) {
        val completedCount = progress.count {
            it.stage == QuestStage.COMPLETED && it.questId > MAIN_QUEST_COUNT
        }
        Log.d(tag, "Found $completedCount completed side quests")

        SIDE_QUEST_THRESHOLDS.forEach { (required, achievementId) ->
            if (completedCount >= required) {
                Log.d(tag, "Unlocking side quest achievement $achievementId for $required quests")
                achievementDao.updateAchievementStatus(achievementId, true)
            }
        }
    }

    private suspend fun checkLevelAchievements(character: GameCharacter) {
        Log.d(tag, "Checking level achievements for level ${character.level}")
        LEVEL_THRESHOLDS.forEach { (level, achievementId) ->
            if (character.level >= level) {
                Log.d(tag, "Unlocking level achievement $achievementId for level $level")
                achievementDao.updateAchievementStatus(achievementId, true)
            }
        }
    }

    private suspend fun checkExperienceAchievements(character: GameCharacter) {
        Log.d(tag, "Checking XP achievements for ${character.experience} XP")
        XP_THRESHOLDS.forEach { (xp, achievementId) ->
            if (character.experience >= xp) {
                Log.d(tag, "Unlocking XP achievement $achievementId for $xp XP")
                achievementDao.updateAchievementStatus(achievementId, true)
            }
        }
    }

    private suspend fun checkLegendAchievement() {
        Log.d(tag, "Checking legend achievement")
        val allAchievements = achievementDao.getAllAchievements()
        val allPreviousUnlocked = allAchievements
            .filter { it.id < LEGEND_ACHIEVEMENT_ID }
            .all { it.reached }

        if (allPreviousUnlocked) {
            Log.i(tag, "Unlocking legend achievement - all previous achievements completed!")
            achievementDao.updateAchievementStatus(LEGEND_ACHIEVEMENT_ID, true)
        }
    }

    companion object {
        private const val MAIN_QUEST_COUNT = 10
        private const val LEGEND_ACHIEVEMENT_ID = 25

        private val SIDE_QUEST_THRESHOLDS = mapOf(
            1 to 11, 3 to 12, 5 to 13, 10 to 14, 25 to 15
        )
        private val LEVEL_THRESHOLDS = mapOf(
            5 to 16, 10 to 17, 15 to 18, 20 to 19, 25 to 20
        )
        private val XP_THRESHOLDS = mapOf(
            100 to 21, 1000 to 22, 5000 to 23, 10000 to 24
        )
    }
}
