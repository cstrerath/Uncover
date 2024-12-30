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
    MAP(R.string.menu_map, { it.navigateToMap() }),
    CHARACTER_CREATION(R.string.menu_character_creation, { it.navigateToCharacterCreation() }),
    LOCATIONS(R.string.menu_locations, { it.navigateToLocations() }),
    PLAYER_STATS(R.string.menu_player_stats, { it.navigateToPlayerStats() }),
    ACHIEVEMENT(R.string.menu_achievements, { it.navigateToAchievement()})
}
