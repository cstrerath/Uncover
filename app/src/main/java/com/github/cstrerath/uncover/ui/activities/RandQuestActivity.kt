package com.github.cstrerath.uncover.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.repository.CharacterRepository
import com.github.cstrerath.uncover.domain.character.progression.XpManager
import com.github.cstrerath.uncover.domain.quest.randquest.RandQuestProgressHandler
import com.github.cstrerath.uncover.ui.base.BaseActivity
import com.github.cstrerath.uncover.ui.screens.quests.randquests.RandQuestScreen
import com.github.cstrerath.uncover.ui.theme.UncoverTheme
import com.github.cstrerath.uncover.ui.viewmodels.RandQuestViewModel
import com.github.cstrerath.uncover.utils.resources.ResourceProvider

class RandQuestActivity : BaseActivity() {
    private lateinit var randQuestViewModel: RandQuestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locationId = intent.getIntExtra(getString(R.string.quest_location_id), -1)
        initializeViewModel()

        setContent {
            UncoverTheme {
                RandQuestScreen(
                    locationId = locationId,
                    viewModel = randQuestViewModel,
                    onQuestComplete = { finish() }
                )
            }
        }
    }

    private fun initializeViewModel() {
        val database = AppDatabase.getInstance(this)
        val resourceProvider = ResourceProvider(this)

        randQuestViewModel = RandQuestViewModel(
            resourceProvider = resourceProvider,
            randQuestProgressHandler = RandQuestProgressHandler(
                this,
                database.characterQuestProgressDao(),
                database.randomQuestDatabaseDao(),
                XpManager(CharacterRepository(this))
            ),
            characterDao = database.gameCharacterDao(),
            characterProgressDao = database.characterQuestProgressDao(),
            randQuestDatabaseDao = database.randomQuestDatabaseDao()
        )
    }
}