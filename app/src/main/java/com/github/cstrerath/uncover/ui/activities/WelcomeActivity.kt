package com.github.cstrerath.uncover.ui.activities

import android.content.Intent
import androidx.compose.runtime.Composable
import com.github.cstrerath.uncover.ui.base.NoBackActivity
import com.github.cstrerath.uncover.ui.screens.welcome.WelcomeScreen


class WelcomeActivity : NoBackActivity() {
    @Composable
    override fun NoBackContent() {
        WelcomeScreen {
            startActivity(Intent(this@WelcomeActivity, CharacterCreationActivity::class.java))
            finish()
        }
    }
}


