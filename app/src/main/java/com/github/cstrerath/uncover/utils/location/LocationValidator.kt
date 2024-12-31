package com.github.cstrerath.uncover.utils.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import org.osmdroid.util.BoundingBox
import java.net.HttpURLConnection
import java.net.URL

// app/src/main/java/com/example/uncover/utils/location/LocationValidator.kt
class LocationValidator(private val context: Context) {
    private val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val boundingBox = BoundingBox(
        49.56415, 8.55226,
        49.42672, 8.38477
    )

    fun isInternetAvailable(): Boolean {
        return try {
            val connection = URL("https://www.google.com")
                .openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            connection.connect()
            connection.responseCode == 200
        } catch (e: Exception) {
            false
        }
    }

    fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun getCurrentLocation(): Location? {
        return try {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            } else null
        } catch (e: Exception) {
            null
        }
    }

    fun isLocationInBounds(location: Location): Boolean {
        return location.latitude <= boundingBox.latNorth &&
                location.latitude >= boundingBox.latSouth &&
                location.longitude >= boundingBox.lonWest &&
                location.longitude <= boundingBox.lonEast
    }
}
