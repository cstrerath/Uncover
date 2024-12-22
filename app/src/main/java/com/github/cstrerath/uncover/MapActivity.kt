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
import org.osmdroid.util.BoundingBox
import org.osmdroid.config.Configuration
import org.osmdroid.views.overlay.Polygon
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.result.contract.ActivityResultContracts
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import android.location.Location



class MapActivity : ComponentActivity() {
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Berechtigung erteilt, zeige Karte
                setContent { MapScreen() }
            }
            else -> {
                // Berechtigung verweigert, beende Activity
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE))

        // Prüfe Berechtigungen vor dem Setzen des Contents
        checkLocationPermissions()
    }

    private fun checkLocationPermissions() {
        when {
            checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED -> {
                // Berechtigungen vorhanden, zeige Karte
                setContent { MapScreen() }
            }
            shouldShowRequestPermissionRationale(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                // Optional: Hier später Erklärung anzeigen
                requestLocationPermissions()
            }
            else -> {
                // Frage Berechtigungen an
                requestLocationPermissions()
            }
        }
    }

    private fun requestLocationPermissions() {
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))
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

                // Benutzerdefiniertes Location Overlay
                val myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), this).apply {
                    enableMyLocation()
                    enableFollowLocation()
                    isDrawAccuracyEnabled = true

                    // Benutzerdefiniertes Icon skalieren
                    val originalBitmap = BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.player_map_marker // Dein Icon
                    )
                    val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 256, 256, true) // Skalierung auf 48x48 Pixel
                    setPersonIcon(scaledBitmap)

                    // Optional: Bewegungsicon ebenfalls setzen
                    setDirectionIcon(scaledBitmap)

                    // Ankerpunkte setzen (zentriert)
                    setPersonAnchor(0.5f, 0.5f)
                    setDirectionAnchor(0.5f, 0.5f)
                }

                overlays.add(0, myLocationOverlay)

                // Stark erweiterte nicht spielbare Bereiche
                val nonPlayableAreas = listOf(
                    mapOf(
                        "north" to 49.67415,
                        "east" to 8.39477,
                        "south" to 49.31672,
                        "west" to 8.24477
                    ),
                    mapOf(
                        "north" to 49.67415,
                        "east" to 8.69226,
                        "south" to 49.31672,
                        "west" to 8.54226
                    ),
                    mapOf(
                        "north" to 49.67415,
                        "east" to 8.69226,
                        "south" to 49.55415,
                        "west" to 8.24477
                    ),
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
