package com.github.cstrerath.uncover

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.config.Configuration

class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE))

        setContent {
            MapScreen()
        }
    }
}

@Composable
fun MapScreen() {
    AndroidView(
        factory = { context ->
            MapView(context).apply {
                setMultiTouchControls(true)
                controller.setZoom(9.5)
                controller.setCenter(GeoPoint(48.8583, 2.2944)) // Paris, auf Mannheim ändern also (49.4889, 8.4692)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
