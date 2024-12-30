package com.github.cstrerath.uncover.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import com.github.cstrerath.uncover.ui.base.BaseActivity
import com.github.cstrerath.uncover.ui.screens.welcome.WelcomeScreen
import com.github.cstrerath.uncover.ui.theme.UncoverTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WelcomeActivity : BaseActivity() {
    private val scope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UncoverTheme {
                WelcomeScreen { initQuestsAndNavigate() }
            }
        }
    }

    private fun initQuestsAndNavigate() {
        scope.launch {
            try {
                coroutineScope {
                    launch { questManager.processNextQuest() }
                    launch { randQuestManager.processNextRandQuest() }
                }

                withContext(Dispatchers.Main) {
                    startActivity(Intent(this@WelcomeActivity, MainMenuActivity::class.java))
                    finish()
                }
            } catch (e: Exception) {
                Log.e("WelcomeActivity", "Error initializing quests: ${e.message}")
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}

