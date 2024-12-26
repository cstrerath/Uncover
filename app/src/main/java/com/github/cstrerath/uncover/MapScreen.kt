package com.github.cstrerath.uncover

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.views.MapView
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


@Composable
fun MapScreen() {
    val scope = rememberCoroutineScope()

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

                // Lade Quest Marker in Coroutine
                scope.launch {
                    loadQuestMarkers(
                        ids = listOf(1, 2, 3), // Beispiel-IDs
                        context = context,
                        mapView = this@apply,
                        locationOverlay = locationOverlay
                    )
                }

                // Füge Non-Playable Areas hinzu
                overlays.addAll(NonPlayableAreasOverlay().createOverlays())
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
    // Karte aktualisieren nach dem Hinzufügen der Marker
    mapView.invalidate()
}
