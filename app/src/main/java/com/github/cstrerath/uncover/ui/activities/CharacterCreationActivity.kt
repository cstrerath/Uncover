package com.github.cstrerath.uncover.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.entities.CharacterClass
import com.github.cstrerath.uncover.domain.character.creation.CharacterCreationLogger
import com.github.cstrerath.uncover.domain.character.creation.PlayerCharacterCreator
import com.github.cstrerath.uncover.domain.character.models.CharacterStatsProvider
import com.github.cstrerath.uncover.domain.quest.QuestProgressInitializer
import com.github.cstrerath.uncover.ui.base.BaseActivity
import com.github.cstrerath.uncover.ui.screens.character.CharacterCreationScreen
import com.github.cstrerath.uncover.ui.theme.UncoverTheme
import kotlinx.coroutines.launch

class CharacterCreationActivity : BaseActivity() {
    private val characterCreator: PlayerCharacterCreator by lazy {
        createCharacterCreator()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UncoverTheme {
                CharacterCreationScreen(
                    onCharacterCreated = ::handleCharacterCreation
                )
            }
        }
    }

    private fun createCharacterCreator(): PlayerCharacterCreator {
        val db = AppDatabase.getInstance(applicationContext)
        return PlayerCharacterCreator(
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
                navigateToMainMenu()
            }
        }
    }

    private fun navigateToMainMenu() {
        startActivity(Intent(this, MainMenuActivity::class.java))
        finish()
    }
}

