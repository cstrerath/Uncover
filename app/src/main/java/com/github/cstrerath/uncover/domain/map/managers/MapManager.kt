package com.github.cstrerath.uncover.domain.map.managers

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import com.github.cstrerath.uncover.domain.map.overlays.FogOfWarOverlay
import com.github.cstrerath.uncover.domain.map.overlays.NonPlayableAreasOverlay
import com.github.cstrerath.uncover.domain.map.overlays.PlayerLocationOverlay
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapManager(private val context: Context) {
    fun setupMapOverlays(mapView: MapView, isDarkMode: Boolean): MyLocationNewOverlay {
        val playerLocation = PlayerLocationOverlay(context, mapView)
        val locationOverlay = playerLocation.getOverlay()

        if (isDarkMode) {
            mapView.overlayManager.tilesOverlay.setColorFilter(
                ColorMatrixColorFilter(
                    ColorMatrix().apply {
                        setScale(0.3f, 0.3f, 0.3f, 1f)
                    }
                )
            )
        }

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
