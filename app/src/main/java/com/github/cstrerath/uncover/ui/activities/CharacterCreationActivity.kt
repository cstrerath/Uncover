package com.github.cstrerath.uncover.ui.activities

import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.entities.CharacterClass
import com.github.cstrerath.uncover.domain.character.creation.CharacterCreationLogger
import com.github.cstrerath.uncover.domain.character.creation.PlayerCharacterCreator
import com.github.cstrerath.uncover.domain.character.models.CharacterStatsProvider
import com.github.cstrerath.uncover.domain.quest.mainquest.QuestProgressInitializer
import com.github.cstrerath.uncover.ui.base.NoBackActivity
import com.github.cstrerath.uncover.ui.screens.character.CharacterCreationScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterCreationActivity : NoBackActivity() {
    private val db by lazy { AppDatabase.getInstance(applicationContext) }
    private val characterCreator by lazy {
        PlayerCharacterCreator(
            context = applicationContext,
            characterDao = db.gameCharacterDao(),
            questProgressInitializer = QuestProgressInitializer(db.characterQuestProgressDao()),
            statsProvider = CharacterStatsProvider(),
            logger = CharacterCreationLogger(db.characterQuestProgressDao())
        )
    }

    @Composable
    override fun NoBackContent() {
        CharacterCreationScreen(
            onCharacterCreated = ::handleCharacterCreation
        )
    }

    private fun handleCharacterCreation(name: String, characterClass: CharacterClass?) {
        Log.d(TAG, "Creating character: $name with class: $characterClass")
        characterClass?.let { selectedClass ->
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    createCharacterAndInitQuests(name, selectedClass)
                }
                navigateToMainMenu()
            }
        }
    }

    private suspend fun createCharacterAndInitQuests(name: String, characterClass: CharacterClass) {
        try {
            characterCreator.createPlayerCharacter(name, characterClass)
            questManager.processNextQuest()
            randQuestManager.processNextRandQuest()
            Log.d(TAG, "Character creation successful")
        } catch (e: Exception) {
            Log.e(TAG, "Error creating character", e)
        }
    }

    private fun navigateToMainMenu() {
        startActivity(Intent(this, MainMenuActivity::class.java))
        finish()
    }

    companion object {
        private const val TAG = "CharacterCreationAct"
    }
}
