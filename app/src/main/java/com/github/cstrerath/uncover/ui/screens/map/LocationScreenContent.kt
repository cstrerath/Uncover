package com.github.cstrerath.uncover.ui.screens.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.github.cstrerath.uncover.utils.location.LocationValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class LocationScreenController(
    private val locationValidator: LocationValidator
) {
    fun checkLocationStatus(
        dialogStates: LocationDialogStates,
        hasLocationPermission: Boolean
    ) {
        val isEnabled = locationValidator.isLocationEnabled()
        dialogStates.isLocationEnabled.value = isEnabled

        val currentLocation = locationValidator.getCurrentLocation()
        if (currentLocation == null && hasLocationPermission && isEnabled) {
            dialogStates.noLocationAvailable.value = true
        } else {
            currentLocation?.let { location ->
                dialogStates.isOutOfBounds.value = !locationValidator.isLocationInBounds(location)
            }
        }
    }
}

@Composable
fun LocationValidationScreen(
    locationValidator: LocationValidator,
    hasLocationPermission: Boolean,
    internetCheckKey: Int,
    locationCheckKey: Int
) {
    val dialogStates = rememberLocationDialogStates()
    val controller = remember { LocationScreenController(locationValidator) }

    LaunchedEffect(internetCheckKey) {
        withContext(Dispatchers.IO) {
            dialogStates.internetAvailable.value = locationValidator.isInternetAvailable()
        }
    }

    LaunchedEffect(locationCheckKey) {
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
    when {
        !states.internetAvailable.value -> {
            NoInternetDialog(
                onDismiss = { states.internetAvailable.value = true }
            )
        }
        (!hasLocationPermission && states.showNoPermissionDialog.value) -> {
            PermissionDialog(
                onDismiss = { states.showNoPermissionDialog.value = false }
            )
        }
        (!states.isLocationEnabled.value && states.showLocationServiceDialog.value) -> {
            LocationServiceDialog(
                onDismiss = { states.showLocationServiceDialog.value = false }
            )
        }
        (states.noLocationAvailable.value && states.showNoLocationDialog.value) -> {
            NoLocationDialog(
                onDismiss = { states.showNoLocationDialog.value = false }
            )
        }
        (states.isOutOfBounds.value && states.showOutOfBoundsDialog.value) -> {
            OutOfBoundsDialog(
                onDismiss = { states.showOutOfBoundsDialog.value = false }
            )
        }
    }
}