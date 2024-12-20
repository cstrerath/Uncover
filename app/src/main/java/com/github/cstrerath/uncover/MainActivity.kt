package com.github.cstrerath.uncover

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            DatabaseInitializer(applicationContext).initializedDatabase()
        }

        setContent {
            MainScreen(onNavigateToCharacterList = {
                startActivity(Intent(this, DatabaseActivity::class.java))
            }, onNavigateToMap = {
                startActivity(Intent(this, MapActivity::class.java))
            })
        }
    }
}

@Composable
fun MainScreen(onNavigateToMap: () -> Unit, onNavigateToCharacterList: () -> Unit) {
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
    }
}