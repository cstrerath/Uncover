package com.github.cstrerath.uncover

object QuestResources {
    private val questTextMap = mapOf(
        // Quest Names
        "quest_1" to R.string.quest_1,
        "quest_2" to R.string.quest_2,
        "quest_3" to R.string.quest_3,
        "quest_4" to R.string.quest_4,
        "quest_5" to R.string.quest_5,
        "quest_6" to R.string.quest_6,
        "quest_7" to R.string.quest_7,
        "quest_8" to R.string.quest_8,
        "quest_9" to R.string.quest_9,
        "quest_10" to R.string.quest_10,
        // Quest 1
        "quest_1_warrior_initial" to R.string.quest_1_warrior_initial,
        "quest_1_thief_initial" to R.string.quest_1_thief_initial,
        "quest_1_mage_initial" to R.string.quest_1_mage_initial,
        "quest_1_warrior_solution" to R.string.quest_1_warrior_solution,
        "quest_1_thief_solution" to R.string.quest_1_thief_solution,
        "quest_1_mage_solution" to R.string.quest_1_mage_solution,
        "quest_1_warrior_completion" to R.string.quest_1_warrior_completion,
        "quest_1_thief_completion" to R.string.quest_1_thief_completion,
        "quest_1_mage_completion" to R.string.quest_1_mage_completion,

        // Quest 2
        "quest_2_warrior_initial" to R.string.quest_2_warrior_initial,
        "quest_2_thief_initial" to R.string.quest_2_thief_initial,
        "quest_2_mage_initial" to R.string.quest_2_mage_initial,
        "quest_2_warrior_solution" to R.string.quest_2_warrior_solution,
        "quest_2_thief_solution" to R.string.quest_2_thief_solution,
        "quest_2_mage_solution" to R.string.quest_2_mage_solution,
        "quest_2_warrior_completion" to R.string.quest_2_warrior_completion,
        "quest_2_thief_completion" to R.string.quest_2_thief_completion,
        "quest_2_mage_completion" to R.string.quest_2_mage_completion,

        // Quest 3
        "quest_3_warrior_initial" to R.string.quest_3_warrior_initial,
        "quest_3_thief_initial" to R.string.quest_3_thief_initial,
        "quest_3_mage_initial" to R.string.quest_3_mage_initial,
        "quest_3_warrior_solution" to R.string.quest_3_warrior_solution,
        "quest_3_thief_solution" to R.string.quest_3_thief_solution,
        "quest_3_mage_solution" to R.string.quest_3_mage_solution,
        "quest_3_warrior_completion" to R.string.quest_3_warrior_completion,
        "quest_3_thief_completion" to R.string.quest_3_thief_completion,
        "quest_3_mage_completion" to R.string.quest_3_mage_completion,

        // Quest 4
        "quest_4_warrior_initial" to R.string.quest_4_warrior_initial,
        "quest_4_thief_initial" to R.string.quest_4_thief_initial,
        "quest_4_mage_initial" to R.string.quest_4_mage_initial,
        "quest_4_warrior_solution" to R.string.quest_4_warrior_solution,
        "quest_4_thief_solution" to R.string.quest_4_thief_solution,
        "quest_4_mage_solution" to R.string.quest_4_mage_solution,
        "quest_4_warrior_completion" to R.string.quest_4_warrior_completion,
        "quest_4_thief_completion" to R.string.quest_4_thief_completion,
        "quest_4_mage_completion" to R.string.quest_4_mage_completion,

        // Quest 5
        "quest_5_warrior_initial" to R.string.quest_5_warrior_initial,
        "quest_5_thief_initial" to R.string.quest_5_thief_initial,
        "quest_5_mage_initial" to R.string.quest_5_mage_initial,
        "quest_5_warrior_solution" to R.string.quest_5_warrior_solution,
        "quest_5_thief_solution" to R.string.quest_5_thief_solution,
        "quest_5_mage_solution" to R.string.quest_5_mage_solution,
        "quest_5_warrior_completion" to R.string.quest_5_warrior_completion,
        "quest_5_thief_completion" to R.string.quest_5_thief_completion,
        "quest_5_mage_completion" to R.string.quest_5_mage_completion,

        // Quest 6
        "quest_6_warrior_initial" to R.string.quest_6_warrior_initial,
        "quest_6_thief_initial" to R.string.quest_6_thief_initial,
        "quest_6_mage_initial" to R.string.quest_6_mage_initial,
        "quest_6_warrior_solution" to R.string.quest_6_warrior_solution,
        "quest_6_thief_solution" to R.string.quest_6_thief_solution,
        "quest_6_mage_solution" to R.string.quest_6_mage_solution,
        "quest_6_warrior_completion" to R.string.quest_6_warrior_completion,
        "quest_6_thief_completion" to R.string.quest_6_thief_completion,
        "quest_6_mage_completion" to R.string.quest_6_mage_completion,

        // Quest 7
        "quest_7_warrior_initial" to R.string.quest_7_warrior_initial,
        "quest_7_thief_initial" to R.string.quest_7_thief_initial,
        "quest_7_mage_initial" to R.string.quest_7_mage_initial,
        "quest_7_warrior_solution" to R.string.quest_7_warrior_solution,
        "quest_7_thief_solution" to R.string.quest_7_thief_solution,
        "quest_7_mage_solution" to R.string.quest_7_mage_solution,
        "quest_7_warrior_completion" to R.string.quest_7_warrior_completion,
        "quest_7_thief_completion" to R.string.quest_7_thief_completion,
        "quest_7_mage_completion" to R.string.quest_7_mage_completion,

        // Quest 8
        "quest_8_warrior_initial" to R.string.quest_8_warrior_initial,
        "quest_8_thief_initial" to R.string.quest_8_thief_initial,
        "quest_8_mage_initial" to R.string.quest_8_mage_initial,
        "quest_8_warrior_solution" to R.string.quest_8_warrior_solution,
        "quest_8_thief_solution" to R.string.quest_8_thief_solution,
        "quest_8_mage_solution" to R.string.quest_8_mage_solution,
        "quest_8_warrior_completion" to R.string.quest_8_warrior_completion,
        "quest_8_thief_completion" to R.string.quest_8_thief_completion,
        "quest_8_mage_completion" to R.string.quest_8_mage_completion,

        // Quest 9
        "quest_9_warrior_initial" to R.string.quest_9_warrior_initial,
        "quest_9_thief_initial" to R.string.quest_9_thief_initial,
        "quest_9_mage_initial" to R.string.quest_9_mage_initial,
        "quest_9_warrior_solution" to R.string.quest_9_warrior_solution,
        "quest_9_thief_solution" to R.string.quest_9_thief_solution,
        "quest_9_mage_solution" to R.string.quest_9_mage_solution,
        "quest_9_warrior_completion" to R.string.quest_9_warrior_completion,
        "quest_9_thief_completion" to R.string.quest_9_thief_completion,
        "quest_9_mage_completion" to R.string.quest_9_mage_completion,

        // Quest 10
        "quest_10_warrior_initial" to R.string.quest_10_warrior_initial,
        "quest_10_thief_initial" to R.string.quest_10_thief_initial,
        "quest_10_mage_initial" to R.string.quest_10_mage_initial,
        "quest_10_warrior_solution" to R.string.quest_10_warrior_solution,
        "quest_10_thief_solution" to R.string.quest_10_thief_solution,
        "quest_10_mage_solution" to R.string.quest_10_mage_solution,
        "quest_10_warrior_completion" to R.string.quest_10_warrior_completion,
        "quest_10_thief_completion" to R.string.quest_10_thief_completion,
        "quest_10_mage_completion" to R.string.quest_10_mage_completion
    )

    fun getQuestTextId(key: String): Int {
        return questTextMap[key] ?: throw IllegalArgumentException("Kein Resource-Mapping für Key: $key")
    }
}
