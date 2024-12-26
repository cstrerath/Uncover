package com.github.cstrerath.uncover

import android.content.Intent
import android.os.Bundle
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

    }
}