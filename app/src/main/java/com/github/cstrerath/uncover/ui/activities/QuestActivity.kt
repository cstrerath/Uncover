package com.github.cstrerath.uncover.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.repository.CharacterRepository
import com.github.cstrerath.uncover.domain.character.progression.XpManager
import com.github.cstrerath.uncover.domain.quest.mainquest.QuestProgressHandler
import com.github.cstrerath.uncover.ui.base.BaseActivity
import com.github.cstrerath.uncover.ui.screens.quests.mainquests.QuestScreen
import com.github.cstrerath.uncover.ui.viewmodels.QuestViewModel
import com.github.cstrerath.uncover.utils.resources.ResourceProvider


class QuestActivity : BaseActivity() {
    private lateinit var questViewModel: QuestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locationId = intent.getIntExtra(getString(R.string.quest_location_id), -1)
        initializeViewModel()

        setContent {
            QuestScreen(
                locationId = locationId,
                viewModel = questViewModel,
                onQuestComplete = { finish() }
            )
        }
    }

    private fun initializeViewModel() {
        val database = AppDatabase.getInstance(this)
        val resourceProvider = ResourceProvider(this)

        questViewModel = QuestViewModel(
            resourceProvider = resourceProvider,
            questProgressHandler = QuestProgressHandler(
                database.characterQuestProgressDao(),
                database.questDao(),
                XpManager(CharacterRepository(this))
            ),
            questDao = database.questDao(),
            questStepDao = database.questStepDao(),
            characterDao = database.gameCharacterDao(),
            characterProgressDao = database.characterQuestProgressDao()
        )
    }
}