package com.github.cstrerath.uncover.ui.activities

import android.content.Intent
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
import kotlinx.coroutines.launch

class CharacterCreationActivity : NoBackActivity() {
    private val characterCreator: PlayerCharacterCreator by lazy {
        createCharacterCreator()
    }

    @Composable
    override fun NoBackContent() {
        CharacterCreationScreen(
            onCharacterCreated = ::handleCharacterCreation
        )
    }

    private fun createCharacterCreator(): PlayerCharacterCreator {
        val db = AppDatabase.getInstance(applicationContext)
        return PlayerCharacterCreator(
            context = applicationContext,
            characterDao = db.gameCharacterDao(),
            questProgressInitializer = QuestProgressInitializer(db.characterQuestProgressDao()),
            statsProvider = CharacterStatsProvider(),
            logger = CharacterCreationLogger(db.characterQuestProgressDao())
        )
    }

    private fun handleCharacterCreation(name: String, characterClass: CharacterClass?) {
        lifecycleScope.launch {
            characterClass?.let {
                characterCreator.createPlayerCharacter(name, it)
                questManager.processNextQuest()
                randQuestManager.processNextRandQuest()
                navigateToMainMenu()
            }
        }
    }

    private fun navigateToMainMenu() {
        startActivity(Intent(this, MainMenuActivity::class.java))
        finish()
    }
}
