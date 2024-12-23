package com.github.cstrerath.uncover

import android.graphics.*
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.Projection
import kotlin.math.cos
import kotlin.math.PI

class FogOfWarOverlay(
    private val playerLocationProvider: () -> GeoPoint?,
    private var visibilityRadiusMeters: Float = 500f
) : Overlay() {

    private val fogPaint = Paint().apply {
        color = Color.argb(192, 128, 128, 128)
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val clearPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    fun setVisibilityRadius(radiusMeters: Float) {
        visibilityRadiusMeters = radiusMeters
    }

    override fun draw(canvas: Canvas?, mapView: MapView?, shadow: Boolean) {
        if (shadow || canvas == null || mapView == null) return

        val projection = mapView.projection
        val playerLocation = playerLocationProvider() ?: return

        val fogBitmap = Bitmap.createBitmap(
            canvas.width,
            canvas.height,
            Bitmap.Config.ARGB_8888
        )
        val fogCanvas = Canvas(fogBitmap)

        fogCanvas.drawRect(
            0f,
            0f,
            canvas.width.toFloat(),
            canvas.height.toFloat(),
            fogPaint
        )

        val playerPoint = projection.toPixels(playerLocation, null)
        val radiusPixels = metersToPixels(
            visibilityRadiusMeters,
            playerLocation.latitude,
            mapView.zoomLevelDouble
        )

        fogCanvas.drawCircle(
            playerPoint.x.toFloat(),
            playerPoint.y.toFloat(),
            radiusPixels,
            clearPaint
        )

        canvas.drawBitmap(fogBitmap, 0f, 0f, null)
        fogBitmap.recycle()

        mapView.postInvalidateDelayed(1000)
    }

    private fun metersToPixels(
        meters: Float,
        latitude: Double,
        zoom: Double
    ): Float {
        // Berechnung der Pixel pro Meter basierend auf Zoom und Latitude
        val pixelsPerTile = 256
        val circumference = 40075016.686
        val metersPerPixel = circumference * cos(latitude * PI / 180.0) / (pixelsPerTile * Math.pow(2.0, zoom))
        return (meters / metersPerPixel).toFloat()
    }
}
