// ui/screens/MainMenuScreen.kt
package com.github.cstrerath.uncover.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.cstrerath.uncover.data.database.entities.Achievement
import com.github.cstrerath.uncover.domain.quest.mainquest.QuestManager
import com.github.cstrerath.uncover.domain.quest.randquest.RandQuestManager
import com.github.cstrerath.uncover.ui.components.QuestProgressButton
import com.github.cstrerath.uncover.ui.components.RandQuestProgressButton
import com.github.cstrerath.uncover.ui.navigation.MenuItems
import com.github.cstrerath.uncover.utils.navigation.NavigationManager

// ui/screens/MainMenuScreen.kt
@Composable
fun MainMenuScreen(
    navigationManager: NavigationManager,
    questManager: QuestManager,
    randQuestManager: RandQuestManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        MenuItems.entries.forEach { menuItem ->
            MenuButton(
                textResId = menuItem.titleResId,
                onClick = { menuItem.action(navigationManager) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        QuestProgressButton(questManager)
        RandQuestProgressButton(randQuestManager)
    }
}

@Composable
fun MenuButton(
    @StringRes textResId: Int,
    onClick: () -> Unit
) {
    Button(onClick = onClick) {
        Text(text = stringResource(textResId))
    }
}
