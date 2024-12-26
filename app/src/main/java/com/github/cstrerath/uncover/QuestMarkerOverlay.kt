package com.github.cstrerath.uncover

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay

class QuestMarkerOverlay(
    private val latitude: Double,
    private val longitude: Double,
    private val playerLocationProvider: () -> GeoPoint?,
    private val visibilityRadiusMeters: Float = 200f,
    context: Context
) : Overlay() {

    private val questLocation = GeoPoint(latitude, longitude)
    private val paint = Paint().apply {
        isAntiAlias = true
        isDither = true
    }

    private val activeMarker: Bitmap = getBitmapFromVectorDrawable(context, R.drawable.quest_marker)
    private val inactiveMarker: Bitmap = getBitmapFromVectorDrawable(context, R.drawable.inactive_quest_marker)

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

    private fun calculateDistance(point1: GeoPoint, point2: GeoPoint): Double {
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

        val marker = if (isVisible) activeMarker else inactiveMarker

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
        super.onDetach(mapView)
    }
}
