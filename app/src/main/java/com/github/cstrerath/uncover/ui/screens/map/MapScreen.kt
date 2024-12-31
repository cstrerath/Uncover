package com.github.cstrerath.uncover.ui.screens.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.compose.runtime.State
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle

import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner


private const val TAG_SCREEN = "MapScreen"

@Composable
fun MapScreen(questLauncher: ActivityResultLauncher<Intent>) {
    Log.d(TAG_SCREEN, "Initializing Map Screen")
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val isDarkMode = isSystemInDarkTheme()
    val controller = remember { MapScreenController(context, scope) }

    // Neue Permission States
    val hasLocationPermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Reagiere auf Änderungen der Location Permission
    LaunchedEffect(hasLocationPermission.value) {
        Log.d(TAG_SCREEN, "Location permission changed: ${hasLocationPermission.value}")
        controller.refreshMapData(questLauncher)
    }

    // Initial Load
    LaunchedEffect(Unit) {
        Log.d(TAG_SCREEN, "Triggering initial map data refresh")
        controller.refreshMapData(questLauncher)
    }

    AndroidView(
        factory = { controller.createMapView(isDarkMode) },
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
private fun rememberLocationPermissionState(context: Context): State<Boolean> {
    val permissionState = remember {
        mutableStateOf(checkLocationPermission(context))
    }

    DisposableEffect(context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                permissionState.value = checkLocationPermission(context)
            }
        }

        val lifecycle = (context as? LifecycleOwner)?.lifecycle
        lifecycle?.addObserver(observer)

        onDispose {
            lifecycle?.removeObserver(observer)
        }
    }

    return permissionState
}

private fun checkLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

