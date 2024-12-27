package com.github.cstrerath.uncover

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.jvm.Throws

class QuestActivity : ComponentActivity() {
    private lateinit var questProgressManager: QuestProgressManager
    private lateinit var questDao: QuestDao
    private lateinit var locationDao: LocationDao
    private lateinit var characterDao: GameCharacterDao
    private lateinit var characterProgressDao: CharacterQuestProgressDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locationId = intent.getIntExtra(getString(R.string.quest_location_id), -1)

        val database = AppDatabase.getInstance(this)
        questProgressManager = QuestProgressManager(database.characterQuestProgressDao(), database.questDao())
        questDao = database.questDao()
        locationDao = database.locationDao()
        characterDao = database.gameCharacterDao()
        characterProgressDao = database.characterQuestProgressDao()

        setContent {
            QuestContent(locationId)
        }
    }

    @Composable
    fun QuestContent(locationId: Int) {
        var questInfo by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(locationId) {
            questInfo = getQuestInfo(locationId)
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = questInfo ?: "Loading...",
                fontSize = 24.sp
            )
        }
    }

    private suspend fun getQuestInfo(locationId: Int): String {
        return withContext(Dispatchers.IO) {
            val player = characterDao.getPlayerCharacter()
            val activeQuest = player?.id?.let { characterProgressDao.getFirstIncompleteQuest(it) }

            if (activeQuest != null) {
                val quest = questDao.getQuestById(activeQuest.questId)
                "Main Quest: ${quest.resourceKey} at $locationId"
            } else {
                "Location: $locationId (No active quest)"
            }
        }
    }
}
