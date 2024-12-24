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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    companion object {
        private var isFirstLaunch = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UncoverTheme {
                if (isFirstLaunch) {
                    SplashScreen()

                    LaunchedEffect(true) {
                        val loginManager = LoginManager(applicationContext)
                        delay(2000)
                        val hasPlayerCharacter = loginManager.performInitialCheck()

                        val intent = if (hasPlayerCharacter) {
                            Intent(this@MainActivity, MainMenuActivity::class.java)
                        } else {
                            Intent(this@MainActivity, CharacterCreationActivity::class.java)
                        }

                        isFirstLaunch = false
                        startActivity(intent)
                        finish()
                    }
                } else {
                    LaunchedEffect(true) {
                        startActivity(Intent(this@MainActivity, MainMenuActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }
}