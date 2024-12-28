package com.github.cstrerath.uncover.domain.map.overlays

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.github.cstrerath.uncover.R
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class PlayerLocationOverlay(context: Context, mapView: MapView) {
    private val overlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView).apply {
        enableMyLocation()
        enableFollowLocation()
        isDrawAccuracyEnabled = true
        configurePlayerIcon(context)
    }

    private fun MyLocationNewOverlay.configurePlayerIcon(context: Context) {
        val originalBitmap = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.player_map_marker
        )
        val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 256, 256, true)
        setPersonIcon(scaledBitmap)
        setDirectionIcon(scaledBitmap)
        setPersonAnchor(0.5f, 0.5f)
        setDirectionAnchor(0.5f, 0.5f)
    }

    fun getOverlay() = overlay
}