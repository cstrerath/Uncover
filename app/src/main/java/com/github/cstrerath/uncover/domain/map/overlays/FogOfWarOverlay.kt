package com.github.cstrerath.uncover.domain.map.overlays

import android.graphics.*
import android.util.Log
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.Projection
import kotlin.math.cos
import kotlin.math.PI
import kotlin.math.pow

class FogOfWarOverlay(
    private val playerLocationProvider: () -> GeoPoint?,
    private var visibilityRadiusMeters: Float = DEFAULT_VISIBILITY_RADIUS
) : Overlay() {
    private val tag = "FogOfWarOverlay"
    private val fogPaint = createFogPaint()
    private val clearPaint = createClearPaint()

    override fun draw(canvas: Canvas?, mapView: MapView?, shadow: Boolean) {
        if (shouldSkipDrawing(canvas, mapView, shadow)) return

        try {
            val projection = mapView!!.projection
            val playerLocation = playerLocationProvider() ?: getFallbackLocation()

            drawFogOfWar(canvas!!, mapView, projection, playerLocation)
            scheduleNextUpdate(mapView)
        } catch (e: Exception) {
            Log.e(tag, "Error drawing fog of war: ${e.message}")
        }
    }

    private fun getFallbackLocation(): GeoPoint {
        return GeoPoint(49.57415, 8.46477)
    }

    private fun shouldSkipDrawing(canvas: Canvas?, mapView: MapView?, shadow: Boolean): Boolean =
        shadow || canvas == null || mapView == null

    private fun createFogPaint() = Paint().apply {
        color = FOG_COLOR
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private fun createClearPaint() = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private fun drawFogOfWar(
        canvas: Canvas,
        mapView: MapView,
        projection: Projection,
        playerLocation: GeoPoint
    ) {
        val fogBitmap = createFogBitmap(canvas.width, canvas.height)
        val fogCanvas = Canvas(fogBitmap)

        drawFogLayer(fogCanvas, canvas)
        drawVisibilityCircle(fogCanvas, projection, playerLocation, mapView)

        canvas.drawBitmap(fogBitmap, 0f, 0f, null)
        fogBitmap.recycle()
    }

    private fun createFogBitmap(width: Int, height: Int): Bitmap =
        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    private fun drawFogLayer(fogCanvas: Canvas, originalCanvas: Canvas) {
        fogCanvas.drawRect(
            0f,
            0f,
            originalCanvas.width.toFloat(),
            originalCanvas.height.toFloat(),
            fogPaint
        )
    }

    private fun drawVisibilityCircle(
        fogCanvas: Canvas,
        projection: Projection,
        playerLocation: GeoPoint,
        mapView: MapView
    ) {
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
    }

    private fun metersToPixels(
        meters: Float,
        latitude: Double,
        zoom: Double
    ): Float {
        val latitudeRadians = latitude * PI / 180.0
        val metersPerPixel = EARTH_CIRCUMFERENCE * cos(latitudeRadians) /
                (PIXELS_PER_TILE * 2.0.pow(zoom))
        return (meters / metersPerPixel).toFloat()
    }

    private fun scheduleNextUpdate(mapView: MapView) {
        mapView.postInvalidateDelayed(UPDATE_DELAY_MS)
    }

    companion object {
        private const val DEFAULT_VISIBILITY_RADIUS = 500f
        private const val FOG_ALPHA = 192
        private const val FOG_RGB = 128
        private val FOG_COLOR = Color.argb(FOG_ALPHA, FOG_RGB, FOG_RGB, FOG_RGB)
        private const val PIXELS_PER_TILE = 256
        private const val EARTH_CIRCUMFERENCE = 40075016.686
        private const val UPDATE_DELAY_MS = 1000L
    }
}
