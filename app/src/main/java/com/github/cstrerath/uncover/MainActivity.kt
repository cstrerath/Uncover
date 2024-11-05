package com.github.cstrerath.uncover

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.room.Room

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "my-database"
        ).build()

        setContent {
            MainScreen {
                startActivity(Intent(this, MapActivity::class.java))
            }
        }
    }
}

@Composable
fun MainScreen(onNavigateToMap: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = onNavigateToMap,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text("Zur Karte")
        }
    }
}