package com.github.cstrerath.uncover.domain.achievement

import android.content.Context
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.entities.CharacterQuestProgress
import com.github.cstrerath.uncover.data.database.entities.GameCharacter
import com.github.cstrerath.uncover.data.database.entities.QuestStage

class AchievementManager(context: Context) {
    private val database = AppDatabase.getInstance(context)
    private val achievementDao = database.achievementDao()
    private val questProgressDao = database.characterQuestProgressDao()
    private val gameCharacterDao = database.gameCharacterDao()

    suspend fun checkAndUpdateAchievements() {
        val playerChar = gameCharacterDao.getPlayerCharacter()
        val questProgress = playerChar?.id?.let { questProgressDao.getCharacterProgress(it) }


        if (questProgress != null) {
            checkMainQuestAchievements(questProgress)
        }
        if (questProgress != null) {
            checkSideQuestAchievements(questProgress)
        }
        if (playerChar != null) {
            checkLevelAchievements(playerChar)
        }
        if (playerChar != null) {
            checkExperienceAchievements(playerChar)
        }
        checkLegendAchievement()
    }

    private suspend fun checkMainQuestAchievements(progress: List<CharacterQuestProgress>) {
        for (questId in 1..10) {
            val completed = progress.any {
                it.questId == questId && it.stage == QuestStage.COMPLETED
            }
            if (completed) {
                achievementDao.updateAchievementStatus(questId, true)
            }
        }
    }

    private suspend fun checkSideQuestAchievements(progress: List<CharacterQuestProgress>) {
        val completedCount = progress.count {
            it.stage == QuestStage.COMPLETED && it.questId > 10
        }

        val thresholds = mapOf(
            1 to 11,   // 1 Nebenquest
            3 to 12,   // 3 Nebenquests
            5 to 13,   // 5 Nebenquests
            10 to 14,  // 10 Nebenquests
            25 to 15   // 25 Nebenquests
        )

        thresholds.forEach { (required, achievementId) ->
            if (completedCount >= required) {
                achievementDao.updateAchievementStatus(achievementId, true)
            }
        }
    }


    private suspend fun checkLevelAchievements(character: GameCharacter) {
        val levelThresholds = mapOf(
            5 to 16,
            10 to 17,
            15 to 18,
            20 to 19,
            25 to 20
        )

        levelThresholds.forEach { (level, achievementId) ->
            if (character.level >= level) {
                achievementDao.updateAchievementStatus(achievementId, true)
            }
        }
    }

    private suspend fun checkExperienceAchievements(character: GameCharacter) {
        val xpThresholds = mapOf(
            100 to 21,
            1000 to 22,
            5000 to 23,
            10000 to 24
        )

        xpThresholds.forEach { (xp, achievementId) ->
            if (character.experience >= xp) {
                achievementDao.updateAchievementStatus(achievementId, true)
            }
        }
    }

    private suspend fun checkLegendAchievement() {
        val allAchievements = achievementDao.getAllAchievements()
        val allPreviousUnlocked = allAchievements
            .filter { it.id < 25 }
            .all { it.reached }

        if (allPreviousUnlocked) {
            achievementDao.updateAchievementStatus(25, true)
        }
    }
}
