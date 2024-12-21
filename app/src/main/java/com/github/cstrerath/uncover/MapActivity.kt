package com.github.cstrerath.uncover

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.BoundingBox
import org.osmdroid.config.Configuration
import org.osmdroid.views.overlay.Polygon


class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE))
        setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                MapScreen()
                FadeOverlay()
            }
        }
    }
}

@Composable
fun FadeOverlay() {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fade-Breite definieren
        val fadeWidth = 50f

        // Linker Fade
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(Color.Black, Color.Transparent),
                startX = 0f,
                endX = fadeWidth
            ),
            topLeft = Offset.Zero,
            size = Size(fadeWidth, size.height)
        )

        // Rechter Fade
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(Color.Transparent, Color.Black),
                startX = size.width - fadeWidth,
                endX = size.width
            ),
            topLeft = Offset(size.width - fadeWidth, 0f),
            size = Size(fadeWidth, size.height)
        )

        // Oberer Fade
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Black, Color.Transparent),
                startY = 0f,
                endY = fadeWidth
            ),
            topLeft = Offset.Zero,
            size = Size(size.width, fadeWidth)
        )

        // Unterer Fade
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.Black),
                startY = size.height - fadeWidth,
                endY = size.height
            ),
            topLeft = Offset(0f, size.height - fadeWidth),
            size = Size(size.width, fadeWidth)
        )
    }
}

@Composable
fun MapScreen() {
    AndroidView(
        factory = { context ->
            MapView(context).apply {
                setMultiTouchControls(true)

                // Scrollbarer Bereich (mit Puffer)
                val scrollableBoundingBox = BoundingBox(
                    49.56415,
                    8.55226,
                    49.42672,
                    8.38477
                )

                setScrollableAreaLimitDouble(scrollableBoundingBox)
                minZoomLevel = 13.0
                maxZoomLevel = 20.0
                controller.setCenter(GeoPoint(49.4889, 8.4692))
                controller.setZoom(14.0)

                // Stark erweiterte nicht spielbare Bereiche
                val nonPlayableAreas = listOf(
                    // Westlicher nicht spielbarer Bereich
                    mapOf(
                        "north" to 49.67415,
                        "east" to 8.39477,
                        "south" to 49.31672,
                        "west" to 8.24477
                    ),
                    // Östlicher nicht spielbarer Bereich
                    mapOf(
                        "north" to 49.67415,
                        "east" to 8.69226,
                        "south" to 49.31672,
                        "west" to 8.54226
                    ),
                    // Nördlicher nicht spielbarer Bereich
                    mapOf(
                        "north" to 49.67415,
                        "east" to 8.69226,
                        "south" to 49.55415,
                        "west" to 8.24477
                    ),
                    // Südlicher nicht spielbarer Bereich
                    mapOf(
                        "north" to 49.43672,
                        "east" to 8.69226,
                        "south" to 49.31672,
                        "west" to 8.24477
                    )
                )

                nonPlayableAreas.forEach { area ->
                    val polygon = Polygon().apply {
                        points = listOf(
                            GeoPoint(area["north"]!!, area["west"]!!),
                            GeoPoint(area["north"]!!, area["east"]!!),
                            GeoPoint(area["south"]!!, area["east"]!!),
                            GeoPoint(area["south"]!!, area["west"]!!),
                            GeoPoint(area["north"]!!, area["west"]!!)
                        )

                        getFillPaint().color = 0xFF000000.toInt() // Komplett schwarz
                        getOutlinePaint().apply {
                            color = 0xFF000000.toInt() // Schwarzer Rand
                            strokeWidth = 0.0f // Kein sichtbarer Rand
                        }
                    }
                    overlays.add(polygon)
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
