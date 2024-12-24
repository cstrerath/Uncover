package com.github.cstrerath.uncover

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QuestStepDao {
    @Query("SELECT * FROM quest_steps WHERE questId = :questId")
    fun getStepsForQuest(questId: Int): List<QuestStep>

    @Query("SELECT * FROM quest_steps WHERE questId = :questId AND stepType = :stepType")
    fun getStepForQuestAndType(questId: Int, stepType: StepType): QuestStep

    @Insert
    fun insertQuestStep(questStep: QuestStep)
}