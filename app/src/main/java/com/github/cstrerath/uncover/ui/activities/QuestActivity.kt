package com.github.cstrerath.uncover.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.repository.CharacterRepository
import com.github.cstrerath.uncover.domain.character.progression.XpManager
import com.github.cstrerath.uncover.domain.quest.mainquest.QuestProgressHandler
import com.github.cstrerath.uncover.ui.base.BaseActivity
import com.github.cstrerath.uncover.ui.screens.UncoverBaseScreen
import com.github.cstrerath.uncover.ui.screens.quests.mainquests.QuestScreen
import com.github.cstrerath.uncover.ui.theme.UncoverTheme
import com.github.cstrerath.uncover.ui.viewmodels.QuestViewModel
import com.github.cstrerath.uncover.utils.resources.ResourceProvider


class QuestActivity : BaseActivity() {
    private lateinit var questViewModel: QuestViewModel
    private val db by lazy { AppDatabase.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Creating Quest Activity")
        val locationId = intent.getIntExtra(getString(R.string.quest_location_id), -1)
        Log.d(TAG, "Location ID: $locationId")

        initializeViewModel()
        setupContent(locationId)
    }

    private fun setupContent(locationId: Int) {
        setContent {
            UncoverTheme {
                UncoverBaseScreen(
                    content = {
                        QuestScreen(
                            locationId = locationId,
                            viewModel = questViewModel,
                            onQuestComplete = {
                                Log.d(TAG, "Quest completed, finishing activity")
                                finish()
                            }
                        )
                    }
                )
            }
        }
    }

    private fun initializeViewModel() {
        Log.d(TAG, "Initializing Quest ViewModel")
        questViewModel = QuestViewModel(
            resourceProvider = ResourceProvider(this),
            questProgressHandler = createQuestProgressHandler(),
            questDao = db.questDao(),
            questStepDao = db.questStepDao(),
            characterDao = db.gameCharacterDao(),
            characterProgressDao = db.characterQuestProgressDao()
        )
    }

    private fun createQuestProgressHandler(): QuestProgressHandler {
        return QuestProgressHandler(
            db.characterQuestProgressDao(),
            db.questDao(),
            XpManager(CharacterRepository(this))
        )
    }

    companion object {
        private const val TAG = "QuestActivity"
    }
}
