package com.github.cstrerath.uncover

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.lifecycleScope
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch


class MainMenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UncoverTheme {
                MainScreen(
                    onNavigateToMap = {
                        startActivity(Intent(this, MapActivity::class.java))
                    },
                    onNavigateToCharacterList = {
                        startActivity(Intent(this, DatabaseActivity::class.java))
                    },
                    onNavigateToCharacterInit = {
                        startActivity(Intent(this, CharacterCreationActivity::class.java))
                    },
                    onNavigateToLocationList = {
                        startActivity(Intent(this, LocationListActivity::class.java))
                    }
                )
            }
        }
    }
}

@Composable
fun MainScreen(onNavigateToMap: () -> Unit, onNavigateToCharacterList: () -> Unit, onNavigateToCharacterInit: () -> Unit, onNavigateToLocationList: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        val database = AppDatabase.getInstance(context = LocalContext.current)
        val gameCharDao = database.gameCharacterDao()
        val questProgress = database.characterQuestProgressDao()
        val questDao = database.questDao()

        Button(
            onClick = onNavigateToMap,
        ) {
            Text("Zur Karte")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateToCharacterList,
        ) {
            Text("View Character List")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateToCharacterInit,
        ) {
            Text("View Character Init")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateToLocationList,
        ) {
            Text("View Locations")
        }

        Spacer(modifier = Modifier.height(16.dp))

        val coroutineScope = rememberCoroutineScope()

        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        val playerId = gameCharDao.getPlayerCharacterId() ?: throw Exception("No player found")
                        Log.d("QuestProgress", "Player ID: $playerId")

                        val activeQuest = questProgress.getFirstIncompleteQuest(playerId)
                        Log.d("QuestProgress", "Active Quest: $activeQuest")

                        if (activeQuest == null) {
                            Log.i("QuestProgress", "No incomplete quests found")
                            return@launch
                        }

                        val activeQuestId = activeQuest.questId
                        Log.d("QuestProgress", "Active Quest ID: $activeQuestId")

                        val questProgressManager = QuestProgressManager(progressDao = questProgress, questDao)
                        questProgressManager.progressQuest(playerId, activeQuestId)

                        val newProgress = questProgress.getQuestProgress(playerId, activeQuestId)
                        Log.i("QuestProgress", "New Progress: $newProgress")

                        // Überprüfen Sie hier, ob eine neue Quest erstellt wurde
                        val nextQuest = questProgress.getFirstIncompleteQuest(playerId)
                        Log.d("QuestProgress", "Next Quest: $nextQuest")

                        // Ausgabe aller Quests des Charakters
                        val allQuests = questProgress.getCharacterProgress(playerId)
                        Log.d("QuestProgress", "All Quests for Character:")
                        allQuests.forEach { quest ->
                            Log.d("QuestProgress", "Quest ID: ${quest.questId}, Stage: ${quest.stage}")
                        }

                    } catch (e: Exception) {
                        Log.e("QuestProgress", "Error updating quest progress: ${e.message}")
                    }
                }
            }
        ) {
            Text("Quest-Progress")
        }


    }
}