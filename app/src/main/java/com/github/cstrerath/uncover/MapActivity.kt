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
                // Aktiviere Multi-Touch
                setMultiTouchControls(true)

                // Definiere die BoundingBox für Mannheim/Ludwigshafen
                val boundingBox = BoundingBox(
                    49.5400, // Nord
                    8.5500,  // Ost
                    49.4000, // Süd
                    8.3500   // West
                )

                // Setze die Grenzen
                setScrollableAreaLimitDouble(boundingBox)

                // Minimale/Maximale Zoomstufen
                minZoomLevel = 13.0
                maxZoomLevel = 20.0

                // Startposition (Zentrum von Mannheim)
                controller.setCenter(GeoPoint(49.4889, 8.4692))
                controller.setZoom(14.0)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}