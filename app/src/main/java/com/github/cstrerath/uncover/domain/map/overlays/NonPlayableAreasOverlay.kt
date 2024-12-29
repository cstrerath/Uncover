package com.github.cstrerath.uncover.domain.map.overlays

import com.github.cstrerath.uncover.domain.map.models.MapBounds
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polygon

class NonPlayableAreasOverlay {
    private val areas = listOf(
        MapBounds(49.67415, 8.39477, 49.31672, 8.24477),
        MapBounds(49.67415, 8.69226, 49.31672, 8.54226),
        MapBounds(49.67415, 8.69226, 49.55415, 8.24477),
        MapBounds(49.43672, 8.69226, 49.31672, 8.24477)
    )

    fun createOverlays(): List<Polygon> = areas.map { createPolygon(it) }

    private fun createPolygon(bounds: MapBounds) = Polygon().apply {
        points = createPolygonPoints(bounds)
        configurePaint()
    }

    private fun createPolygonPoints(bounds: MapBounds) = listOf(
        GeoPoint(bounds.north, bounds.west),
        GeoPoint(bounds.north, bounds.east),
        GeoPoint(bounds.south, bounds.east),
        GeoPoint(bounds.south, bounds.west),
        GeoPoint(bounds.north, bounds.west)
    )

    private fun Polygon.configurePaint() {
        fillPaint.color = 0xFF000000.toInt()
        getOutlinePaint().apply {
            color = 0xFF000000.toInt()
            strokeWidth = 0.0f
        }
    }
}