package com.github.cstrerath.uncover.domain.quest.randquest.generators

import android.content.Context
import android.util.Log
import com.github.cstrerath.uncover.R

class NameGenerator(private val context: Context) {
    private val tag = "NameGenerator"

    // Fixed resource IDs are preferred over getIdentifier() for better build optimization
    private val names = listOf(
        context.getString(R.string.random_character_1),
        context.getString(R.string.random_character_2),
        context.getString(R.string.random_character_3),
        context.getString(R.string.random_character_4),
        context.getString(R.string.random_character_5),
        context.getString(R.string.random_character_6),
        context.getString(R.string.random_character_7),
        context.getString(R.string.random_character_8),
        context.getString(R.string.random_character_9),
        context.getString(R.string.random_character_10),
        context.getString(R.string.random_character_11),
        context.getString(R.string.random_character_12),
        context.getString(R.string.random_character_13),
        context.getString(R.string.random_character_14),
        context.getString(R.string.random_character_15),
        context.getString(R.string.random_character_16),
        context.getString(R.string.random_character_17),
        context.getString(R.string.random_character_18),
        context.getString(R.string.random_character_19),
        context.getString(R.string.random_character_20),
        context.getString(R.string.random_character_21),
        context.getString(R.string.random_character_22),
        context.getString(R.string.random_character_23),
        context.getString(R.string.random_character_24),
        context.getString(R.string.random_character_25)
    )

    fun getRandomName(): String {
        return try {
            names.random().also {
                Log.d(tag, "Generated random name: $it")
            }
        } catch (e: Exception) {
            Log.e(tag, "Error generating random name: ${e.message}")
            context.getString(R.string.fallback_character_name)
        }
    }
}