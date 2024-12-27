package com.github.cstrerath.uncover

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.views.MapView
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import androidx.compose.runtime.*

@Composable
fun MapScreen() {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var playerCharacter by remember { mutableStateOf<GameCharacter?>(null) }

    AndroidView(
        factory = { context ->
            MapView(context).apply {
                // Konfiguriere Grundeinstellungen
                MapConfiguration(this).configure()

                // Füge Player Location Overlay hinzu
                val playerLocation = PlayerLocationOverlay(context, this)
                val locationOverlay = playerLocation.getOverlay()
                overlays.add(0, locationOverlay)

                // Füge Fog of War hinzu
                overlays.add(FogOfWarOverlay(
                    playerLocationProvider = { locationOverlay.myLocation },
                    visibilityRadiusMeters = 200f
                ))

                // Füge Non-Playable Areas hinzu
                overlays.addAll(NonPlayableAreasOverlay().createOverlays())

                // Lade Quest Marker wenn Player Character verfügbar
                scope.launch {
                    val database = AppDatabase.getInstance(context)
                    playerCharacter = database.gameCharacterDao().getPlayerCharacter()

                    playerCharacter?.let { player ->
                        val progressManager = QuestProgressManager(database.characterQuestProgressDao(),database.questDao())
                        val activeLocationIds = progressManager.getActiveQuestLocations(player.id)

                        loadQuestMarkers(
                            ids = activeLocationIds,
                            context = context,
                            mapView = this@apply,
                            locationOverlay = locationOverlay
                        )
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

private suspend fun loadQuestMarkers(
    ids: List<Int>,
    context: Context,
    mapView: MapView,
    locationOverlay: MyLocationNewOverlay
) {
    val database = AppDatabase.getInstance(context)
    val locationDao = database.locationDao()

    ids.forEach { id ->
        locationDao.getLocation(id)?.let { location ->
            mapView.overlays.add(QuestMarkerOverlay(
                latitude = location.latitude,
                longitude = location.longitude,
                playerLocationProvider = { locationOverlay.myLocation },
                context = context
            ))
        }
    }
    mapView.invalidate()
}
