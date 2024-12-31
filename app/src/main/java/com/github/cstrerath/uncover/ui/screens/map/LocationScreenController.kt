package com.github.cstrerath.uncover.ui.screens.map

import android.util.Log
import com.github.cstrerath.uncover.utils.location.LocationValidator

private const val TAG = "LocationScreenController"

class LocationScreenController(
    private val locationValidator: LocationValidator
) {
    fun checkLocationStatus(
        dialogStates: LocationDialogStates,
        hasLocationPermission: Boolean
    ) {
        Log.d(TAG, "Starting location status check with permission: $hasLocationPermission")
        dialogStates.logCurrentState()

        val isEnabled = locationValidator.isLocationEnabled()
        dialogStates.isLocationEnabled.value = isEnabled
        Log.d(TAG, "Location services enabled: $isEnabled")

        val currentLocation = locationValidator.getCurrentLocation()
        if (currentLocation == null && hasLocationPermission && isEnabled) {
            Log.d(TAG, "No location available despite permissions and enabled services")
            dialogStates.noLocationAvailable.value = true
        } else {
            currentLocation?.let { location ->
                val inBounds = locationValidator.isLocationInBounds(location)
                dialogStates.isOutOfBounds.value = !inBounds
                Log.d(TAG, "Location in bounds: $inBounds")
            }
        }
        dialogStates.logCurrentState()
    }
}

