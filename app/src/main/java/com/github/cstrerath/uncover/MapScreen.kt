package com.github.cstrerath.uncover

import android.content.Context
import android.content.Intent
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope

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
                            locationOverlay = locationOverlay,
                            onMarkerClick = { questId ->
                                // Hier starten Sie die QuestActivity
                                val intent = Intent(context, QuestActivity::class.java).apply {
                                    putExtra("QUEST_ID", questId)
                                }
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

suspend fun loadQuestMarkers(
    ids: List<Int>,
    context: Context,
    mapView: MapView,
    locationOverlay: MyLocationNewOverlay,
    onMarkerClick: (Int) -> Unit
) {
    withContext(Dispatchers.IO) {
        val database = AppDatabase.getInstance(context)
        val locationDao = database.locationDao()

        ids.forEach { id ->
            val location = locationDao.getLocation(id)
            location?.let {
                withContext(Dispatchers.Main) {
                    val marker = QuestMarkerOverlay(
                        latitude = it.latitude,
                        longitude = it.longitude,
                        playerLocationProvider = { locationOverlay.myLocation },
                        onMarkerClick = { onMarkerClick(id) },
                        context = context
                    )
                    mapView.overlays.add(marker)
                }
            }
        }
        withContext(Dispatchers.Main) {
            mapView.invalidate()
        }
    }
}


