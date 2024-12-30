// ui/navigation/MenuItems.kt
// ui/navigation/MenuItems.kt
package com.github.cstrerath.uncover.ui.navigation

import androidx.annotation.StringRes
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.utils.navigation.NavigationManager

enum class MenuItems(
    @StringRes val titleResId: Int,
    val action: (NavigationManager) -> Unit
) {
    MAP(R.string.start_game, { it.navigateToMap() }),
    PLAYER_STATS(R.string.menu_player_stats, { it.navigateToPlayerStats() }),
    ACHIEVEMENT(R.string.menu_achievements, { it.navigateToAchievement() })
}
