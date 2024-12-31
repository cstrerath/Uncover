package com.github.cstrerath.uncover.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.repository.CharacterRepository
import com.github.cstrerath.uncover.domain.character.progression.XpManager
import com.github.cstrerath.uncover.domain.quest.randquest.RandQuestProgressHandler
import com.github.cstrerath.uncover.ui.base.BaseActivity
import com.github.cstrerath.uncover.ui.screens.UncoverBaseScreen
import com.github.cstrerath.uncover.ui.screens.quests.randquests.RandQuestScreen
import com.github.cstrerath.uncover.ui.theme.UncoverTheme
import com.github.cstrerath.uncover.ui.viewmodels.RandQuestViewModel

class RandQuestActivity : BaseActivity() {
    private lateinit var randQuestViewModel: RandQuestViewModel
    private val db by lazy { AppDatabase.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Creating Random Quest Activity")
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
                        RandQuestScreen(
                            locationId = locationId,
                            viewModel = randQuestViewModel,
                            onQuestComplete = {
                                Log.d(TAG, "Random quest completed, finishing activity")
                                finish()
                            }
                        )
                    }
                )
            }
        }
    }

    private fun initializeViewModel() {
        Log.d(TAG, "Initializing Random Quest ViewModel")
        randQuestViewModel = RandQuestViewModel(
            randQuestProgressHandler = createQuestProgressHandler(),
            characterDao = db.gameCharacterDao(),
            characterProgressDao = db.characterQuestProgressDao(),
            randQuestDatabaseDao = db.randomQuestDatabaseDao()
        )
    }

    private fun createQuestProgressHandler(): RandQuestProgressHandler {
        return RandQuestProgressHandler(
            this,
            db.characterQuestProgressDao(),
            db.randomQuestDatabaseDao(),
            XpManager(CharacterRepository(this))
        )
    }

    companion object {
        private const val TAG = "RandQuestActivity"
    }
}
