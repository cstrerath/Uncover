package com.github.cstrerath.uncover.ui.screens.map

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
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

@Composable
fun MapScreen(questLauncher: ActivityResultLauncher<Intent>) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val isDarkMode = isSystemInDarkTheme()
    val controller = remember { MapScreenController(context, scope) }

    LaunchedEffect(Unit) {
        controller.refreshMapData(questLauncher)
    }

    AndroidView(
        factory = { controller.createMapView(isDarkMode) },
        modifier = Modifier.fillMaxSize()
    )
}
