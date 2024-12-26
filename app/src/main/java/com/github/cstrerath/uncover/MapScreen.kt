package com.github.cstrerath.uncover

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.views.MapView

@Composable
fun MapScreen() {
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

                // Füge Quest Marker hinzu
                overlays.add(QuestMarkerOverlay(
                    latitude = 49.47433,
                    longitude = 8.53472,
                    playerLocationProvider = { locationOverlay.myLocation },
                    context = context
                ))

                // Füge Non-Playable Areas hinzu
                overlays.addAll(NonPlayableAreasOverlay().createOverlays())
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}