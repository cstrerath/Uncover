package com.github.cstrerath.uncover.ui.screens.map

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.mutableStateOf
import com.github.cstrerath.uncover.domain.map.managers.MapManager
import com.github.cstrerath.uncover.domain.map.managers.QuestMarkerHandler
import com.github.cstrerath.uncover.ui.factories.MapViewFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.views.MapView

private const val TAG = "MapScreenController"

class MapScreenController(
    private val context: Context,
    private val scope: CoroutineScope
) {
    private val mapViewState = mutableStateOf<MapView?>(null)
    private val mapManager = MapManager(context)
    private val markerHandler = QuestMarkerHandler(context)

    fun refreshMapData(questLauncher: ActivityResultLauncher<Intent>) {
        Log.d(TAG, "Refreshing map data")
        scope.launch {
            withContext(Dispatchers.IO) {
                try {
                    markerHandler.loadPlayerAndQuestData(mapViewState.value, questLauncher)
                    Log.d(TAG, "Map data refresh completed")
                } catch (e: Exception) {
                    Log.e(TAG, "Error refreshing map data", e)
                }
            }
        }
    }

    fun createMapView(isDarkMode: Boolean): MapView {
        Log.d(TAG, "Creating MapView with dark mode: $isDarkMode")
        return MapViewFactory.create(
            context = context,
            mapViewState = mapViewState,
            mapManager = mapManager,
            isDarkMode = isDarkMode
        )
    }
}
