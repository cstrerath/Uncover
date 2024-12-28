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

import androidx.activity.result.ActivityResultLauncher
import com.github.cstrerath.uncover.data.database.AppDatabase
import com.github.cstrerath.uncover.data.database.entities.GameCharacter
import com.github.cstrerath.uncover.domain.quest.QuestProgressHandler
import com.github.cstrerath.uncover.ui.activities.QuestActivity


@Composable
fun MapScreen(questLauncher: ActivityResultLauncher<Intent>) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var playerCharacter by remember { mutableStateOf<GameCharacter?>(null) }
    var mapView by remember { mutableStateOf<MapView?>(null) }

    AndroidView(
        factory = { context ->
            MapView(context).apply {
                mapView = this
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
                        val progressManager = QuestProgressHandler(database.characterQuestProgressDao(),database.questDao())
                        val activeLocationIds = progressManager.getActiveQuestLocations(player.id)

                        loadQuestMarkers(
                            ids = activeLocationIds,
                            context = context,
                            mapView = this@apply,
                            locationOverlay = locationOverlay,
                            onMarkerClick = { locationId ->
                                val intent = Intent(context, QuestActivity::class.java).apply {
                                    putExtra(context.getString(R.string.quest_location_id), locationId)
                                }
                                questLauncher.launch(intent)
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

        // Bestehende Quest-Marker entfernen
        withContext(Dispatchers.Main) {
            mapView.overlays.removeAll { it is QuestMarkerOverlay }
        }

        ids.forEach { locationId ->
            val location = locationDao.getLocation(locationId)
            location?.let {
                withContext(Dispatchers.Main) {
                    val marker = QuestMarkerOverlay(
                        latitude = it.latitude,
                        longitude = it.longitude,
                        playerLocationProvider = { locationOverlay.myLocation },
                        onMarkerClick = { onMarkerClick(locationId) },
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



