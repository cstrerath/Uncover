// ui/activities/MainActivity.kt
package com.github.cstrerath.uncover.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import com.github.cstrerath.uncover.ui.base.BaseActivity
import com.github.cstrerath.uncover.ui.screens.LaunchScreen
import com.github.cstrerath.uncover.ui.theme.UncoverTheme

class MainActivity : BaseActivity() {
    companion object {
        private var isInitialLaunch = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UncoverTheme {
                LaunchScreen(
                    isInitialLaunch = isInitialLaunch,
                    loginManager = loginManager,
                    navigationManager = navigationManager,
                    onFinish = {
                        isInitialLaunch = false
                        finish()
                    }
                )
            }
        }
    }
}
