package com.github.cstrerath.uncover.ui.activities

import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import com.github.cstrerath.uncover.ui.base.NoBackActivity
import com.github.cstrerath.uncover.ui.screens.welcome.WelcomeScreen


class WelcomeActivity : NoBackActivity() {
    @Composable
    override fun NoBackContent() {
        Log.d(TAG, "Displaying welcome screen")
        WelcomeScreen {
            Log.d(TAG, "Navigating to character creation")
            startActivity(Intent(this@WelcomeActivity, CharacterCreationActivity::class.java))
            finish()
        }
    }

    companion object {
        private const val TAG = "WelcomeActivity"
    }
}
