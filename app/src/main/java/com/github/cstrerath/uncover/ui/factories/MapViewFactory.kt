package com.github.cstrerath.uncover.ui.factories

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import com.github.cstrerath.uncover.domain.map.config.MapConfiguration
import com.github.cstrerath.uncover.domain.map.managers.MapManager
import org.osmdroid.views.MapView

object MapViewFactory {
    private const val TAG = "MapViewFactory"

    fun create(
        context: Context,
        mapViewState: MutableState<MapView?>,
        mapManager: MapManager,
        isDarkMode: Boolean
    ): MapView {
        Log.d(TAG, "Creating new MapView instance with darkMode: $isDarkMode")
        return MapView(context).apply {
            mapViewState.value = this
            configureMap(this, mapManager, isDarkMode)
        }
    }

    private fun configureMap(
        mapView: MapView,
        mapManager: MapManager,
        isDarkMode: Boolean
    ) {
        MapConfiguration(mapView).configure()
        mapManager.setupMapOverlays(mapView, isDarkMode)
        Log.d(TAG, "Map configuration and overlays setup completed")
    }
}

