package com.github.cstrerath.uncover.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.cstrerath.uncover.AppDatabase
import com.github.cstrerath.uncover.CharacterClass
import com.github.cstrerath.uncover.CharacterQuestProgress
import com.github.cstrerath.uncover.CharacterQuestProgressDao
import com.github.cstrerath.uncover.GameCharacterDao
import com.github.cstrerath.uncover.LocationDao
import com.github.cstrerath.uncover.QuestDao
import com.github.cstrerath.uncover.QuestProgressManager
import com.github.cstrerath.uncover.QuestResources
import com.github.cstrerath.uncover.QuestStage
import com.github.cstrerath.uncover.data.database.dao.QuestStepDao
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.StepType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class QuestActivity : ComponentActivity() {
    private lateinit var questProgressManager: QuestProgressManager
    private lateinit var questDao: QuestDao
    private lateinit var questStepDao: QuestStepDao
    private lateinit var locationDao: LocationDao
    private lateinit var characterDao: GameCharacterDao
    private lateinit var characterProgressDao: CharacterQuestProgressDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locationId = intent.getIntExtra(getString(R.string.quest_location_id), -1)

        val database = AppDatabase.getInstance(this)
        questProgressManager = QuestProgressManager(database.characterQuestProgressDao(), database.questDao())
        questDao = database.questDao()
        questStepDao = database.questStepDao()
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
        var activeQuest by remember { mutableStateOf<CharacterQuestProgress?>(null) }
        var playerId by remember { mutableStateOf<String?>(null) }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(locationId) {
            withContext(Dispatchers.IO) {
                questInfo = getQuestInfo(locationId)
                val player = characterDao.getPlayerCharacter()
                playerId = player?.id
                playerId?.let { pid ->
                    activeQuest = characterProgressDao.getFirstIncompleteQuest(pid)
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = questInfo ?: stringResource(R.string.loading),
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            activeQuest?.let { quest ->
                playerId?.let { pid ->
                    Button(
                        onClick = {
                            coroutineScope.launch(Dispatchers.IO) {
                                questProgressManager.progressQuest(pid, quest.questId)
                                // Quest-Info nach Progress aktualisieren
                                withContext(Dispatchers.Main) {
                                    finish()
                                }
                            }
                        }
                    ) {
                        Text(
                            text = when (quest.stage) {
                                QuestStage.AT_START -> "Ich nehme die Herausforderung an!"
                                QuestStage.AT_QUEST_LOCATION -> "Ich stelle mich der Aufgabe!"
                                QuestStage.AT_END -> "Mission erfolgreich abgeschlossen!"
                                else -> "Weiter"
                            }
                        )
                    }
                }
            }
        }
    }



    private suspend fun getQuestInfo(locationId: Int): String {
        return withContext(Dispatchers.IO) {
            val player = characterDao.getPlayerCharacter()
            val playerClass = player?.characterClass
            val activeQuest = player?.id?.let { characterProgressDao.getFirstIncompleteQuest(it) }

            if (activeQuest != null) {
                val quest = questDao.getQuestById(activeQuest.questId)

                val questStep = when (activeQuest.stage) {
                    QuestStage.AT_START -> questStepDao.getStepForQuestAndType(activeQuest.questId,
                        StepType.INITIAL
                    )
                    QuestStage.AT_QUEST_LOCATION -> questStepDao.getStepForQuestAndType(activeQuest.questId,
                        StepType.SOLUTION
                    )
                    QuestStage.AT_END -> questStepDao.getStepForQuestAndType(activeQuest.questId,
                        StepType.COMPLETION
                    )
                    else -> throw IllegalStateException("Unbekannte Quest-Stage: ${activeQuest.stage}")
                }

                val questTextKey = when (playerClass) {
                    CharacterClass.MAGE -> questStep.mageVariantKey
                    CharacterClass.THIEF -> questStep.thiefVariantKey
                    CharacterClass.WARRIOR -> questStep.warriorVariantKey
                    else -> throw IllegalStateException("Unbekannte Charakterklasse: $playerClass")
                }

                val questTextId = QuestResources.getQuestTextId(questTextKey)
                val questNameId = QuestResources.getQuestTextId(quest.resourceKey)

                "Quest: ${getString(questNameId)} - ${getString(questTextId)}"
            } else {
                "Location: $locationId (Keine aktive Quest)"
            }
        }
    }




}
