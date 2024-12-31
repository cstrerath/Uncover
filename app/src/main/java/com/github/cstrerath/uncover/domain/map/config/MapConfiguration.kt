package com.github.cstrerath.uncover.domain.map.config


import android.util.Log
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class MapConfiguration(private val mapView: MapView) {
    private val tag = "MapConfiguration"

    fun configure() {
        Log.d(tag, "Starting map configuration")
        try {
            mapView.apply {
                setMultiTouchControls(true)
                setScrollableAreaLimitDouble(createBoundingBox())
                setZoomLevels()
                setInitialPosition()
            }
            Log.d(tag, "Map configuration completed successfully")
        } catch (e: Exception) {
            Log.e(tag, "Failed to configure map: ${e.message}")
            throw e
        }
    }

    private fun createBoundingBox() = BoundingBox(
        NORTH_BOUND, EAST_BOUND,
        SOUTH_BOUND, WEST_BOUND
    ).also {
        Log.d(tag, "Created bounding box: N=${it.latNorth}, E=${it.lonEast}, S=${it.latSouth}, W=${it.lonWest}")
    }

    private fun setZoomLevels() {
        Log.d(tag, "Setting zoom levels: min=$MIN_ZOOM, max=$MAX_ZOOM")
        mapView.apply {
            minZoomLevel = MIN_ZOOM
            maxZoomLevel = MAX_ZOOM
        }
    }

    private fun setInitialPosition() {
        Log.d(tag, "Setting initial position: lat=$INITIAL_LAT, lon=$INITIAL_LON, zoom=$INITIAL_ZOOM")
        mapView.controller.apply {
            setCenter(GeoPoint(INITIAL_LAT, INITIAL_LON))
            setZoom(INITIAL_ZOOM)
        }
    }

    companion object {
        // Map boundaries for Mannheim area
        private const val NORTH_BOUND = 49.56415
        private const val EAST_BOUND = 8.55226
        private const val SOUTH_BOUND = 49.42672
        private const val WEST_BOUND = 8.38477

        // Zoom constraints
        private const val MIN_ZOOM = 13.0
        private const val MAX_ZOOM = 20.0

        // Initial position (Mannheim center)
        private const val INITIAL_LAT = 49.4836
        private const val INITIAL_LON = 8.4753
        private const val INITIAL_ZOOM = 13.0
    }
}
