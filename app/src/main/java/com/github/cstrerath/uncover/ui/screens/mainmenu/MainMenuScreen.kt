// ui/screens/MainMenuScreen.kt
package com.github.cstrerath.uncover.ui.screens.mainmenu

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.BuildConfig
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.utils.navigation.NavigationManager

private const val TAG = "MainMenuScreen"

@Composable
fun MainMenuScreen(navigationManager: NavigationManager) {
    Log.d(TAG, "Initializing Main Menu Screen")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppTitle()
        AppIcon()
        Spacer(modifier = Modifier.weight(1f))
        MenuContent(navigationManager)
        Spacer(modifier = Modifier.weight(1f))
        VersionDisplay()
    }
}

@Composable
private fun AppTitle() {
    Text(
        text = "Uncover",
        style = MaterialTheme.typography.displayLarge.copy(
            color = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun AppIcon() {
    Image(
        painter = painterResource(id = R.drawable.app_icon),
        contentDescription = null,
        modifier = Modifier
            .size(240.dp)
            .padding(vertical = 16.dp)
    )
}

@Composable
private fun VersionDisplay() {
    Text(
        text = "v${BuildConfig.VERSION_NAME}",
        style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        textAlign = TextAlign.Center
    )
}
