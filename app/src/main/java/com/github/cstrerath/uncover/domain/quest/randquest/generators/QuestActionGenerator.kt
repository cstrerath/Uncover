package com.github.cstrerath.uncover.domain.quest.randquest.generators

import android.content.Context
import com.github.cstrerath.uncover.R

class QuestActionGenerator(private val context: Context) {

    private val questActions = listOf(
        context.getString(R.string.rand_quest_story_1),
        context.getString(R.string.rand_quest_story_2),
        context.getString(R.string.rand_quest_story_3),
        context.getString(R.string.rand_quest_story_4),
        context.getString(R.string.rand_quest_story_5),
        context.getString(R.string.rand_quest_story_6),
        context.getString(R.string.rand_quest_story_7),
        context.getString(R.string.rand_quest_story_8),
        context.getString(R.string.rand_quest_story_9),
        context.getString(R.string.rand_quest_story_10),
        context.getString(R.string.rand_quest_story_11),
        context.getString(R.string.rand_quest_story_12),
        context.getString(R.string.rand_quest_story_13),
        context.getString(R.string.rand_quest_story_14),
        context.getString(R.string.rand_quest_story_15),
        context.getString(R.string.rand_quest_story_16)
    )
    
    fun getRandomQuestStory () : String {
        return questActions.random()
    }
}