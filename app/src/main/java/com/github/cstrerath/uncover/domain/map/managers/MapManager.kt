package com.github.cstrerath.uncover.domain.map.managers

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.Log
import com.github.cstrerath.uncover.domain.map.overlays.FogOfWarOverlay
import com.github.cstrerath.uncover.domain.map.overlays.NonPlayableAreasOverlay
import com.github.cstrerath.uncover.domain.map.overlays.PlayerLocationOverlay
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapManager(private val context: Context) {
    private val tag = "MapManager"

    fun setupMapOverlays(mapView: MapView, isDarkMode: Boolean): MyLocationNewOverlay {
        Log.d(tag, "Setting up map overlays. Dark mode: $isDarkMode")

        try {
            val locationOverlay = createLocationOverlay(mapView)
            applyDarkMode(mapView, isDarkMode)
            setupOverlays(mapView, locationOverlay)

            Log.d(tag, "Map overlays setup completed")
            return locationOverlay
        } catch (e: Exception) {
            Log.e(tag, "Failed to setup map overlays: ${e.message}")
            throw e
        }
    }

    private fun createLocationOverlay(mapView: MapView): MyLocationNewOverlay {
        Log.d(tag, "Creating player location overlay")
        return PlayerLocationOverlay(context, mapView)
            .getOverlay()
            .also { Log.d(tag, "Player location overlay created") }
    }

    private fun applyDarkMode(mapView: MapView, isDarkMode: Boolean) {
        if (isDarkMode) {
            Log.d(tag, "Applying dark mode color filter")
            mapView.overlayManager.tilesOverlay.setColorFilter(
                ColorMatrixColorFilter(
                    ColorMatrix().apply {
                        setScale(DARK_MODE_SCALE, DARK_MODE_SCALE, DARK_MODE_SCALE, 1f)
                    }
                )
            )
        }
    }

    private fun setupOverlays(mapView: MapView, locationOverlay: MyLocationNewOverlay) {
        Log.d(tag, "Adding map overlays")
        mapView.overlays.apply {
            add(0, locationOverlay)
            Log.v(tag, "Added location overlay")

            add(createFogOfWarOverlay(locationOverlay))
            Log.v(tag, "Added fog of war overlay")

            addAll(NonPlayableAreasOverlay().createOverlays())
            Log.v(tag, "Added non-playable areas overlay")
        }
    }

    private fun createFogOfWarOverlay(locationOverlay: MyLocationNewOverlay) =
        FogOfWarOverlay(
            playerLocationProvider = { locationOverlay.myLocation },
            visibilityRadiusMeters = VISIBILITY_RADIUS
        )

    companion object {
        private const val DARK_MODE_SCALE = 0.3f
        private const val VISIBILITY_RADIUS = 200f
    }
}
