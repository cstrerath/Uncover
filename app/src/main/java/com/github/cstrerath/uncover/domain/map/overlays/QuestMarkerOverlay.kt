package com.github.cstrerath.uncover.domain.map.overlays

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
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
    private val visibilityRadiusMeters: Float = 200f,
    private val onMarkerClick: (Int,Boolean) -> Unit,
    context: Context
) : Overlay() {

    private val questLocation = GeoPoint(latitude, longitude)
    private val paint = Paint().apply {
        isAntiAlias = true
        isDither = true
    }

    private val activeMarker: Bitmap = getBitmapFromVectorDrawable(context, R.drawable.quest_marker)
    private val inactiveMarker: Bitmap = getBitmapFromVectorDrawable(context, R.drawable.inactive_quest_marker)

    private val activeRandMarker: Bitmap = getBitmapFromVectorDrawable(context, R.drawable.rand_quest_marker)
    private val inactiveRandMarker: Bitmap = getBitmapFromVectorDrawable(context, R.drawable.inactive_rand_quest_marker)



    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
            ?: throw IllegalStateException("Could not load drawable resource")

        val bitmap = Bitmap.createBitmap(
            128,
            128,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
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
        if (shadow || canvas == null || mapView == null) return

        val projection = mapView.projection
        val questPoint = projection.toPixels(questLocation, null)
        val playerLocation = playerLocationProvider()

        val isVisible = playerLocation?.let { playerPos ->
            calculateDistance(playerPos, questLocation) <= visibilityRadiusMeters
        } ?: false

        //val marker = if (isVisible) activeMarker else inactiveMarker
        val marker = if (location.id < 100) if (isVisible) activeMarker else inactiveMarker else if (isVisible) activeRandMarker else inactiveRandMarker

        canvas.drawBitmap(
            marker,
            (questPoint.x - marker.width / 2).toFloat(),
            (questPoint.y - marker.height / 2).toFloat(),
            paint
        )
    }

    override fun onDetach(mapView: MapView?) {
        activeMarker.recycle()
        inactiveMarker.recycle()
        activeRandMarker.recycle()
        inactiveRandMarker.recycle()
        super.onDetach(mapView)
    }

    override fun onSingleTapConfirmed(e: MotionEvent?, mapView: MapView?): Boolean {
        if (e != null && mapView != null) {
            val projection = mapView.projection
            val tappedPoint = projection.fromPixels(e.x.toInt(),e.y.toInt())
            val distance = calculateDistance(tappedPoint,questLocation)
            val isRandomQuest = location.id < 100

            val hitboxSize = calculateHitboxSize(mapView.zoomLevelDouble.toFloat())

            if (distance <= hitboxSize) {
                val playerLocation = playerLocationProvider()
                if (playerLocation != null && calculateDistance(playerLocation,questLocation) <= visibilityRadiusMeters) {
                    onMarkerClick(location.id,isRandomQuest)
                    return true
                }
            }
        }
        return super.onSingleTapConfirmed(e, mapView)
    }

    private fun calculateHitboxSize(zoomLevel: Float): Float {
        val baseSize = 250f
        val minZoom = 13f
        val maxZoom = 20f

        val normalizedZoom = (zoomLevel - minZoom) / (maxZoom - minZoom)

        val scaleFactor = 1 - normalizedZoom.pow(0.3f)

        val lowZoomBoost = if (normalizedZoom < 0.2) 1.8f else 1f

        return baseSize * scaleFactor * lowZoomBoost
    }

}
