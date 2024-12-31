package com.github.cstrerath.uncover.ui.screens.map

import androidx.compose.runtime.MutableState

data class LocationDialogStates(
    val internetAvailable: MutableState<Boolean>,
    val isLocationEnabled: MutableState<Boolean>,
    val isOutOfBounds: MutableState<Boolean>,
    val noLocationAvailable: MutableState<Boolean>,
    val showOutOfBoundsDialog: MutableState<Boolean>,
    val showLocationServiceDialog: MutableState<Boolean>,
    val showNoLocationDialog: MutableState<Boolean>,
    val showNoPermissionDialog: MutableState<Boolean>
)
