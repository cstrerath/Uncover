package com.github.cstrerath.uncover.ui.factories

import android.content.Context
import androidx.compose.runtime.*
import com.github.cstrerath.uncover.domain.map.config.MapConfiguration
import com.github.cstrerath.uncover.domain.map.managers.MapManager
import org.osmdroid.views.MapView

object MapViewFactory {
    fun create(
        context: Context,
        mapViewState: MutableState<MapView?>,
        mapManager: MapManager
    ): MapView {
        return MapView(context).apply {
            mapViewState.value = this
            MapConfiguration(this).configure()
            mapManager.setupMapOverlays(this)
        }
    }
}
