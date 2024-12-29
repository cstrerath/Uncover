package com.github.cstrerath.uncover.data.database.initialization.data

import com.github.cstrerath.uncover.data.database.entities.QuestStep
import com.github.cstrerath.uncover.data.database.entities.StepType

object QuestStepData {
    fun getAllQuestSteps(): List<QuestStep> {
        return (1..10).flatMap { questId ->
            createQuestSteps(questId)
        }
    }

    private fun createQuestSteps(questId: Int): List<QuestStep> {
        return StepType.entries.mapIndexed { stepIndex, stepType ->
            createQuestStep(questId, stepIndex, stepType)
        }
    }

    private fun createQuestStep(questId: Int, stepIndex: Int, stepType: StepType): QuestStep {
        return QuestStep(
            stepId = calculateStepId(questId, stepIndex),
            questId = questId,
            stepType = stepType,
            warriorVariantKey = buildTextKey(questId, "warrior", stepType),
            thiefVariantKey = buildTextKey(questId, "thief", stepType),
            mageVariantKey = buildTextKey(questId, "mage", stepType)
        )
    }

    private fun calculateStepId(questId: Int, stepIndex: Int): Int {
        val stepsPerQuest = 3
        val baseStepId = (questId - 1) * stepsPerQuest
        return baseStepId + stepIndex + 1
    }

    private fun buildTextKey(questId: Int, characterClass: String, stepType: StepType): String {
        return "quest_${questId}_${characterClass}_${stepType.name.lowercase()}"
    }
}
