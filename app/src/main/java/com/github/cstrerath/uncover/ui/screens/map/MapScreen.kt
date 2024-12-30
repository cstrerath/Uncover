package com.github.cstrerath.uncover.ui.screens.map

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.views.MapView

@Composable
fun MapScreen(questLauncher: ActivityResultLauncher<Intent>) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val mapViewState = remember { mutableStateOf<MapView?>(null) }
    val mapManager = remember { MapManager(context) }
    val markerHandler = remember { QuestMarkerHandler(context) }

    fun refreshMapData() {
        scope.launch {
            withContext(Dispatchers.IO) {
                markerHandler.loadPlayerAndQuestData(mapViewState.value, questLauncher)
            }
        }
    }

    LaunchedEffect(Unit) {
        refreshMapData()
    }

    AndroidView(
        factory = { MapViewFactory.create(context, mapViewState, mapManager) },
        modifier = Modifier.fillMaxSize()
    )
}
