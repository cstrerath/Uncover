package com.github.cstrerath.uncover.domain.map.overlays

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.util.Log
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.data.database.entities.Location
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay
import kotlin.math.pow

class QuestMarkerOverlay(
    private val location: Location,
    latitude: Double,
    longitude: Double,
    private val playerLocationProvider: () -> GeoPoint?,
    private val visibilityRadiusMeters: Float = DEFAULT_VISIBILITY_RADIUS,
    private val onMarkerClick: (Int, Boolean) -> Unit,
    context: Context
) : Overlay() {
    private val tag = "QuestMarkerOverlay"
    private val questLocation = GeoPoint(latitude, longitude)
    private val paint = createPaint()
    private val markers = loadMarkers(context)

    private data class MarkerSet(
        val activeMarker: Bitmap,
        val inactiveMarker: Bitmap,
        val activeRandMarker: Bitmap,
        val inactiveRandMarker: Bitmap
    )

    private fun createPaint() = Paint().apply {
        isAntiAlias = true
        isDither = true
    }

    private fun loadMarkers(context: Context): MarkerSet {
        Log.d(tag, "Loading marker resources")
        return try {
            MarkerSet(
                getBitmapFromVectorDrawable(context, R.drawable.quest_marker),
                getBitmapFromVectorDrawable(context, R.drawable.inactive_quest_marker),
                getBitmapFromVectorDrawable(context, R.drawable.rand_quest_marker),
                getBitmapFromVectorDrawable(context, R.drawable.inactive_rand_quest_marker)
            ).also { Log.d(tag, "Markers loaded successfully") }
        } catch (e: Exception) {
            Log.e(tag, "Failed to load markers: ${e.message}")
            throw e
        }
    }

    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        Log.v(tag, "Converting vector drawable $drawableId to bitmap")
        val drawable = ContextCompat.getDrawable(context, drawableId)
            ?: throw IllegalStateException("Could not load drawable resource $drawableId")

        return Bitmap.createBitmap(
            MARKER_SIZE,
            MARKER_SIZE,
            Bitmap.Config.ARGB_8888
        ).also { bitmap ->
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
        }
    }

    private fun calculateDistance(point1: IGeoPoint, point2: GeoPoint): Double {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(
            point1.latitude, point1.longitude,
            point2.latitude, point2.longitude,
            results
        )
        return results[0].toDouble()
    }

    override fun draw(canvas: Canvas?, mapView: MapView?, shadow: Boolean) {
        if (shouldSkipDrawing(shadow, canvas, mapView)) return

        try {
            drawMarker(canvas!!, mapView!!)
        } catch (e: Exception) {
            Log.e(tag, "Error drawing marker: ${e.message}")
        }
    }

    private fun shouldSkipDrawing(shadow: Boolean, canvas: Canvas?, mapView: MapView?): Boolean =
        shadow || canvas == null || mapView == null

    private fun drawMarker(canvas: Canvas, mapView: MapView) {
        val projection = mapView.projection
        val questPoint = projection.toPixels(questLocation, null)
        val isVisible = isMarkerVisible()

        val marker = selectMarker(isVisible)
        drawMarkerBitmap(canvas, questPoint, marker)
    }

    private fun isMarkerVisible(): Boolean =
        playerLocationProvider()?.let { playerPos ->
            calculateDistance(playerPos, questLocation) <= visibilityRadiusMeters
        } ?: false

    private fun selectMarker(isVisible: Boolean): Bitmap = with(markers) {
        when {
            location.id < RANDOM_QUEST_ID_THRESHOLD && isVisible -> activeMarker
            location.id < RANDOM_QUEST_ID_THRESHOLD -> inactiveMarker
            isVisible -> activeRandMarker
            else -> inactiveRandMarker
        }
    }

    private fun drawMarkerBitmap(canvas: Canvas, questPoint: Point, marker: Bitmap) {
        canvas.drawBitmap(
            marker,
            (questPoint.x - marker.width / 2).toFloat(),
            (questPoint.y - marker.height / 2).toFloat(),
            paint
        )
    }

    override fun onDetach(mapView: MapView?) {
        Log.d(tag, "Detaching overlay and recycling bitmaps")
        with(markers) {
            activeMarker.recycle()
            inactiveMarker.recycle()
            activeRandMarker.recycle()
            inactiveRandMarker.recycle()
        }
        super.onDetach(mapView)
    }

    override fun onSingleTapConfirmed(e: MotionEvent?, mapView: MapView?): Boolean {
        if (e == null || mapView == null) return super.onSingleTapConfirmed(e, mapView)

        return try {
            handleTap(e, mapView)
        } catch (ex: Exception) {
            Log.e(tag, "Error handling tap: ${ex.message}")
            super.onSingleTapConfirmed(e, mapView)
        }
    }

    private fun handleTap(e: MotionEvent, mapView: MapView): Boolean {
        val projection = mapView.projection
        val tappedPoint = projection.fromPixels(e.x.toInt(), e.y.toInt())
        val distance = calculateDistance(tappedPoint, questLocation)
        val hitboxSize = calculateHitboxSize(mapView.zoomLevelDouble.toFloat())

        return if (isValidTap(distance, hitboxSize)) {
            onMarkerClick(location.id, location.id >= RANDOM_QUEST_ID_THRESHOLD)
            true
        } else {
            super.onSingleTapConfirmed(e, mapView)
        }
    }

    private fun isValidTap(distance: Double, hitboxSize: Float): Boolean {
        if (distance > hitboxSize) return false

        return playerLocationProvider()?.let { playerLocation ->
            calculateDistance(playerLocation, questLocation) <= visibilityRadiusMeters
        } ?: false
    }

    private fun calculateHitboxSize(zoomLevel: Float): Float {
        val normalizedZoom = (zoomLevel - MIN_ZOOM) / (MAX_ZOOM - MIN_ZOOM)
        val scaleFactor = 1 - normalizedZoom.pow(HITBOX_SCALE_POWER)
        val lowZoomBoost = if (normalizedZoom < LOW_ZOOM_THRESHOLD) LOW_ZOOM_BOOST else 1f

        return BASE_HITBOX_SIZE * scaleFactor * lowZoomBoost
    }

    companion object {
        private const val MARKER_SIZE = 128
        private const val DEFAULT_VISIBILITY_RADIUS = 200f
        private const val RANDOM_QUEST_ID_THRESHOLD = 100

        private const val BASE_HITBOX_SIZE = 250f
        private const val MIN_ZOOM = 13f
        private const val MAX_ZOOM = 20f
        private const val HITBOX_SCALE_POWER = 0.3f
        private const val LOW_ZOOM_THRESHOLD = 0.2f
        private const val LOW_ZOOM_BOOST = 1.8f
    }
}
