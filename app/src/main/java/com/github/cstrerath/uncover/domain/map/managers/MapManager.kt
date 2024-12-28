package com.github.cstrerath.uncover.domain.map.managers

import android.content.Context
import com.github.cstrerath.uncover.domain.map.overlays.FogOfWarOverlay
import com.github.cstrerath.uncover.domain.map.overlays.NonPlayableAreasOverlay
import com.github.cstrerath.uncover.domain.map.overlays.PlayerLocationOverlay
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapManager(private val context: Context) {
    fun setupMapOverlays(mapView: MapView): MyLocationNewOverlay {
        val playerLocation = PlayerLocationOverlay(context, mapView)
        val locationOverlay = playerLocation.getOverlay()

        mapView.overlays.apply {
            add(0, locationOverlay)
            add(createFogOfWarOverlay(locationOverlay))
            addAll(NonPlayableAreasOverlay().createOverlays())
        }

        return locationOverlay
    }

    private fun createFogOfWarOverlay(locationOverlay: MyLocationNewOverlay) =
        FogOfWarOverlay(
            playerLocationProvider = { locationOverlay.myLocation },
            visibilityRadiusMeters = 200f
        )
}
