package com.github.cstrerath.uncover.ui.screens.map

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.github.cstrerath.uncover.utils.location.LocationValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG_SCREEN = "LocationValidationScreen"

@Composable
fun LocationValidationScreen(
    locationValidator: LocationValidator,
    hasLocationPermission: Boolean,
    internetCheckKey: Int,
    locationCheckKey: Int
) {
    Log.d(TAG_SCREEN, "Initializing location validation screen")
    val dialogStates = rememberLocationDialogStates()
    val controller = remember { LocationScreenController(locationValidator) }

    LaunchedEffect(internetCheckKey) {
        Log.d(TAG_SCREEN, "Checking internet connectivity")
        withContext(Dispatchers.IO) {
            dialogStates.internetAvailable.value = locationValidator.isInternetAvailable()
        }
    }

    LaunchedEffect(locationCheckKey) {
        Log.d(TAG_SCREEN, "Checking location status")
        controller.checkLocationStatus(dialogStates, hasLocationPermission)
    }

    ShowLocationDialogs(
        states = dialogStates,
        hasLocationPermission = hasLocationPermission
    )
}

@Composable
fun rememberLocationDialogStates() = remember {
    LocationDialogStates(
        internetAvailable = mutableStateOf(true),
        isLocationEnabled = mutableStateOf(true),
        isOutOfBounds = mutableStateOf(false),
        noLocationAvailable = mutableStateOf(false),
        showOutOfBoundsDialog = mutableStateOf(true),
        showLocationServiceDialog = mutableStateOf(true),
        showNoLocationDialog = mutableStateOf(true),
        showNoPermissionDialog = mutableStateOf(true)
    )
}

@Composable
fun ShowLocationDialogs(
    states: LocationDialogStates,
    hasLocationPermission: Boolean
) {
    Log.d(TAG_SCREEN, "Showing location dialogs based on states")

    when {
        !states.internetAvailable.value -> {
            Log.d(TAG_SCREEN, "Showing no internet dialog")
            NoInternetDialog(
                onDismiss = { states.internetAvailable.value = true }
            )
        }
        (!hasLocationPermission && states.showNoPermissionDialog.value) -> {
            Log.d(TAG_SCREEN, "Showing permission dialog")
            PermissionDialog(
                onDismiss = { states.showNoPermissionDialog.value = false }
            )
        }
        (!states.isLocationEnabled.value && states.showLocationServiceDialog.value) -> {
            Log.d(TAG_SCREEN, "Showing location service dialog")
            LocationServiceDialog(
                onDismiss = { states.showLocationServiceDialog.value = false }
            )
        }
        (states.noLocationAvailable.value && states.showNoLocationDialog.value) -> {
            Log.d(TAG_SCREEN, "Showing no location dialog")
            NoLocationDialog(
                onDismiss = { states.showNoLocationDialog.value = false }
            )
        }
        (states.isOutOfBounds.value && states.showOutOfBoundsDialog.value) -> {
            Log.d(TAG_SCREEN, "Showing out of bounds dialog")
            OutOfBoundsDialog(
                onDismiss = { states.showOutOfBoundsDialog.value = false }
            )
        }
    }
}
