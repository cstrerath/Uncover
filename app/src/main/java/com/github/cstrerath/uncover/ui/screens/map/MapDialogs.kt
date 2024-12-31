package com.github.cstrerath.uncover.ui.screens.map

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.github.cstrerath.uncover.R

private const val TAG = "MapDialogs"

@Composable
fun NoInternetDialog(onDismiss: () -> Unit) {
    Log.d(TAG, "Showing no internet dialog")
    val context = LocalContext.current

    MapAlertDialog(
        title = stringResource(R.string.map_no_internet),
        text = stringResource(R.string.map_no_internet_info),
        confirmButton = {
            SettingsButton(
                text = stringResource(R.string.map_no_internet_settings),
                onClick = {
                    Log.d(TAG, "Opening wireless settings")
                    context.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                }
            )
        },
        dismissButton = {
            ContinueButton(
                text = stringResource(R.string.map_continue_anyway),
                onDismiss = onDismiss
            )
        }
    )
}

@Composable
fun PermissionDialog(onDismiss: () -> Unit) {
    Log.d(TAG, "Showing permission dialog")
    val context = LocalContext.current

    MapAlertDialog(
        title = stringResource(R.string.map_no_location),
        text = stringResource(R.string.map_no_location_info),
        confirmButton = {
            SettingsButton(
                text = stringResource(R.string.map_no_location_settings),
                onClick = {
                    Log.d(TAG, "Opening app settings")
                    context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    })
                }
            )
        },
        dismissButton = {
            ContinueButton(
                text = stringResource(R.string.map_no_location_dismiss),
                onDismiss = onDismiss
            )
        },
        onDismissRequest = onDismiss
    )
}

@Composable
fun LocationServiceDialog(onDismiss: () -> Unit) {
    Log.d(TAG, "Showing location service dialog")
    val context = LocalContext.current

    MapAlertDialog(
        title = stringResource(R.string.map_location_disabled),
        text = stringResource(R.string.map_location_disabled_info),
        confirmButton = {
            SettingsButton(
                text = stringResource(R.string.map_location_settings),
                onClick = {
                    Log.d(TAG, "Opening location settings")
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            )
        },
        dismissButton = {
            ContinueButton(
                text = stringResource(R.string.map_continue_anyway),
                onDismiss = onDismiss
            )
        },
        onDismissRequest = onDismiss
    )
}

@Composable
fun NoLocationDialog(onDismiss: () -> Unit) {
    Log.d(TAG, "Showing no location signal dialog")
    MapAlertDialog(
        title = stringResource(R.string.map_no_location_signal),
        text = stringResource(R.string.map_no_location_signal_info),
        confirmButton = {
            ContinueButton(
                text = stringResource(R.string.map_continue_anyway),
                onDismiss = onDismiss
            )
        }
    )
}

@Composable
fun OutOfBoundsDialog(onDismiss: () -> Unit) {
    Log.d(TAG, "Showing out of bounds dialog")
    MapAlertDialog(
        title = stringResource(R.string.map_outside_bounds),
        text = stringResource(R.string.map_outside_bounds_info),
        confirmButton = {
            ContinueButton(
                text = stringResource(R.string.map_continue_anyway),
                onDismiss = onDismiss
            )
        }
    )
}

@Composable
private fun MapAlertDialog(
    title: String,
    text: String,
    confirmButton: @Composable () -> Unit,
    dismissButton: (@Composable () -> Unit)? = null,
    onDismissRequest: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = confirmButton,
        dismissButton = dismissButton
    )
}

@Composable
private fun SettingsButton(
    text: String,
    onClick: () -> Unit
) {
    Button(onClick = onClick) {
        Text(text)
    }
}

@Composable
private fun ContinueButton(
    text: String,
    onDismiss: () -> Unit
) {
    Button(
        onClick = {
            Log.d(TAG, "Dialog dismissed")
            onDismiss()
        }
    ) {
        Text(text)
    }
}
