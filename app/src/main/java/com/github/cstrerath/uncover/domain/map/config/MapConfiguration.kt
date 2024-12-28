package com.github.cstrerath.uncover.domain.map.config

import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class MapConfiguration(private val mapView: MapView) {
    fun configure() {
        mapView.apply {
            setMultiTouchControls(true)
            setScrollableAreaLimitDouble(createBoundingBox())
            setZoomLevels()
            setInitialPosition()
        }
    }

    private fun createBoundingBox() = BoundingBox(
        49.56415, 8.55226,
        49.42672, 8.38477
    )

    private fun setZoomLevels() {
        mapView.apply {
            minZoomLevel = 13.0
            maxZoomLevel = 20.0
        }
    }

    private fun setInitialPosition() {
        mapView.controller.apply {
            setCenter(GeoPoint(49.4889, 8.4692))
            setZoom(14.0)
        }
    }
}
