package com.github.cstrerath.uncover.ui.screens.map

import android.util.Log
import androidx.compose.runtime.MutableState

private const val TAG = "LocationDialogStates"

data class LocationDialogStates(
    val internetAvailable: MutableState<Boolean>,
    val isLocationEnabled: MutableState<Boolean>,
    val isOutOfBounds: MutableState<Boolean>,
    val noLocationAvailable: MutableState<Boolean>,
    val showOutOfBoundsDialog: MutableState<Boolean>,
    val showLocationServiceDialog: MutableState<Boolean>,
    val showNoLocationDialog: MutableState<Boolean>,
    val showNoPermissionDialog: MutableState<Boolean>
) {
    fun logCurrentState() {
        Log.d(TAG, "States - Internet: ${internetAvailable.value}, Location: ${isLocationEnabled.value}, OutOfBounds: ${isOutOfBounds.value}, NoLocation: ${noLocationAvailable.value}, ShowDialogs: OutOfBounds=${showOutOfBoundsDialog.value}, Service=${showLocationServiceDialog.value}, NoLoc=${showNoLocationDialog.value}, NoPerm=${showNoPermissionDialog.value}")
    }
}
