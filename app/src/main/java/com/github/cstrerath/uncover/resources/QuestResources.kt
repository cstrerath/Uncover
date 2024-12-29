package com.github.cstrerath.uncover.resources

import com.github.cstrerath.uncover.R

object QuestResources {
    private val questTextMap = buildMap {
        putQuestNames()
        putQuestDetails()
    }

    fun getQuestTextId(key: String): Int {
        return questTextMap[key] ?: throw IllegalArgumentException("No resource mapping found for key: $key")
    }

    private fun MutableMap<String, Int>.putQuestNames() {
        for (questNumber in 1..10) {
            put("quest_$questNumber", getResourceId("quest_$questNumber"))
        }
    }

    private fun MutableMap<String, Int>.putQuestDetails() {
        val characterClasses = listOf("warrior", "thief", "mage")
        val phases = listOf("initial", "solution", "completion")

        for (questNumber in 1..10) {
            for (characterClass in characterClasses) {
                for (phase in phases) {
                    val key = buildString {
                        append("quest_")
                        append(questNumber)
                        append("_")
                        append(characterClass)
                        append("_")
                        append(phase)
                    }
                    put(key, getResourceId(key))
                }
            }
        }
    }

    private fun getResourceId(resourceName: String): Int {
        return R.string::class.java.getField(resourceName).getInt(null)
    }
}