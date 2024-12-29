package com.github.cstrerath.uncover.domain.map.overlays

import android.graphics.*
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import kotlin.math.cos
import kotlin.math.PI
import kotlin.math.pow

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
        val pixelsPerTile = 256
        val circumference = 40075016.686
        val metersPerPixel = circumference * cos(latitude * PI / 180.0) / (pixelsPerTile * 2.0.pow(
            zoom
        ))
        return (meters / metersPerPixel).toFloat()
    }
}
