package com.github.cstrerath.uncover.utils.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import org.osmdroid.util.BoundingBox
import java.net.HttpURLConnection
import java.net.URL

class LocationValidator(private val context: Context) {
    private val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val boundingBox = BoundingBox(
        49.56415, 8.55226,
        49.42672, 8.38477
    )

    companion object {
        private const val TAG = "LocationValidator"
        private const val INTERNET_CHECK_URL = "https://www.google.com"
        private const val CONNECTION_TIMEOUT = 5000
    }

    fun isInternetAvailable(): Boolean {
        Log.d(TAG, "Checking internet connectivity")
        return try {
            val connection = URL(INTERNET_CHECK_URL)
                .openConnection() as HttpURLConnection
            connection.connectTimeout = CONNECTION_TIMEOUT
            connection.connect()
            val isAvailable = connection.responseCode == 200
            Log.d(TAG, "Internet available: $isAvailable")
            isAvailable
        } catch (e: Exception) {
            Log.e(TAG, "Internet check failed", e)
            false
        }
    }

    fun isLocationEnabled(): Boolean {
        val enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        Log.d(TAG, "Location services enabled: $enabled")
        return enabled
    }

    fun getCurrentLocation(): Location? {
        Log.d(TAG, "Attempting to get current location")
        return try {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.also {
                    Log.d(TAG, "Current location: ${it.latitude}, ${it.longitude}")
                }
            } else {
                Log.d(TAG, "Location permission not granted")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting location", e)
            null
        }
    }

    fun isLocationInBounds(location: Location): Boolean {
        val inBounds = location.latitude <= boundingBox.latNorth &&
                location.latitude >= boundingBox.latSouth &&
                location.longitude >= boundingBox.lonWest &&
                location.longitude <= boundingBox.lonEast
        Log.d(TAG, "Location in bounds check: $inBounds")
        return inBounds
    }
}

