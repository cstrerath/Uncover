package com.github.cstrerath.uncover.domain.map.overlays

import android.util.Log
import com.github.cstrerath.uncover.domain.map.models.MapBounds
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polygon

class NonPlayableAreasOverlay {
    private val tag = "NonPlayableAreas"

    private enum class AreaType {
        LEFT, RIGHT, UPPER, LOWER
    }

    private val areas = mapOf(
        AreaType.LEFT to MapBounds(NORTH_BOUND, WEST_OUTER, SOUTH_BOUND, WEST_INNER),
        AreaType.RIGHT to MapBounds(NORTH_BOUND, EAST_BOUND, SOUTH_BOUND, EAST_INNER),
        AreaType.UPPER to MapBounds(NORTH_BOUND, EAST_BOUND, NORTH_INNER, WEST_INNER),
        AreaType.LOWER to MapBounds(SOUTH_INNER, EAST_BOUND, SOUTH_BOUND, WEST_INNER)
    ).values.toList()

    fun createOverlays(): List<Polygon> {
        Log.d(tag, "Creating ${areas.size} non-playable area overlays")
        return areas.mapIndexed { index, bounds ->
            createPolygon(bounds).also {
                Log.v(tag, "Created polygon $index: N=${bounds.north}, E=${bounds.east}")
            }
        }
    }

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
        fillPaint.color = POLYGON_COLOR
        getOutlinePaint().apply {
            color = POLYGON_COLOR
            strokeWidth = STROKE_WIDTH
        }
    }

    companion object {
        // Boundary coordinates for Mannheim area
        private const val NORTH_BOUND = 49.67415
        private const val SOUTH_BOUND = 49.31672
        private const val EAST_BOUND = 8.69226
        private const val WEST_INNER = 8.24477
        private const val EAST_INNER = 8.54226
        private const val NORTH_INNER = 49.55415
        private const val SOUTH_INNER = 49.43672
        private const val WEST_OUTER = 8.39477

        // Polygon appearance
        private const val POLYGON_COLOR = 0xFF000000.toInt()
        private const val STROKE_WIDTH = 0.0f
    }
}
