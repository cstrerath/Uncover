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
                        val questProgressManager = QuestProgressManager(progressDao = questProgress)
                        questProgressManager.progressQuest(playerId,1)
                        val newProgress = questProgress.getQuestProgress(playerId, 1) ?: throw Exception("No quest progress found")
                        Log.i("QuestProgress", newProgress.toString())
                    } catch (e: Exception) {
                        // Hier könnten Sie eine Fehlermeldung anzeigen
                        Log.e("QuestProgress", "Error updating quest progress: ${e.message}")
                    }
                }
            }
        ) {
            Text("Quest-Progress")
        }

    }
}