package com.github.cstrerath.uncover.domain.map.models

import org.osmdroid.util.BoundingBox

/**
 * Represents the geographical boundaries of a map section.
 * All values are in decimal degrees (WGS84).
 */
data class MapBounds(
    val north: Double,
    val east: Double,
    val south: Double,
    val west: Double
) {
    init {
        require(north > south) { "Northern boundary must be greater than southern boundary" }
        require(east > west) { "Eastern boundary must be greater than western boundary" }
    }

    fun toBoundingBox(): BoundingBox = BoundingBox(north, east, south, west)
}
