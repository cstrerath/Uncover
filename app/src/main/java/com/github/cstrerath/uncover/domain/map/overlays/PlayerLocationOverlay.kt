package com.github.cstrerath.uncover.domain.map.overlays

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.github.cstrerath.uncover.R
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class PlayerLocationOverlay(context: Context, mapView: MapView) {
    private val tag = "PlayerLocation"
    private val overlay = createLocationOverlay(context, mapView)

    fun getOverlay() = overlay

    private fun createLocationOverlay(context: Context, mapView: MapView): MyLocationNewOverlay {
        Log.d(tag, "Creating player location overlay")
        return MyLocationNewOverlay(GpsMyLocationProvider(context), mapView).apply {
            enableMyLocation()
            enableFollowLocation()
            isDrawAccuracyEnabled = true
            configurePlayerIcon(context)
            Log.d(tag, "Player location overlay configured")
        }
    }

    private fun MyLocationNewOverlay.configurePlayerIcon(context: Context) {
        try {
            Log.d(tag, "Configuring player icon")
            val originalBitmap = BitmapFactory.decodeResource(
                context.resources,
                R.drawable.player_map_marker
            )
            val scaledBitmap = Bitmap.createScaledBitmap(
                originalBitmap,
                ICON_SIZE,
                ICON_SIZE,
                true
            )
            setPersonIcon(scaledBitmap)
            setDirectionIcon(scaledBitmap)
            setPersonAnchor(ANCHOR_CENTER, ANCHOR_CENTER)
            setDirectionAnchor(ANCHOR_CENTER, ANCHOR_CENTER)
            Log.d(tag, "Player icon configured successfully")
        } catch (e: Exception) {
            Log.e(tag, "Failed to configure player icon: ${e.message}")
            throw e
        }
    }

    companion object {
        private const val ICON_SIZE = 256
        private const val ANCHOR_CENTER = 0.5f
    }
}
