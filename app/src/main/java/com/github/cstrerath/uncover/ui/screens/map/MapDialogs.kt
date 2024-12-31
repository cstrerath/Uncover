package com.github.cstrerath.uncover.ui.screens.map

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.github.cstrerath.uncover.R

@Composable
fun NoInternetDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { },
        title = { Text(stringResource(R.string.map_no_internet)) },
        text = { Text(stringResource(R.string.map_no_internet_info)) },
        confirmButton = {
            Button(
                onClick = {
                    context.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                }
            ) {
                Text(stringResource(R.string.map_no_internet_settings))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.map_continue_anyway))
            }
        }
    )
}

@Composable
fun PermissionDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.map_no_location)) },
        text = { Text(stringResource(R.string.map_no_location_info)) },
        confirmButton = {
            Button(
                onClick = {
                    context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    })
                }
            ) {
                Text(stringResource(R.string.map_no_location_settings))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.map_no_location_dismiss))
            }
        }
    )
}

@Composable
fun LocationServiceDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.map_location_disabled)) },
        text = { Text(stringResource(R.string.map_location_disabled_info)) },
        confirmButton = {
            Button(
                onClick = {
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            ) {
                Text(stringResource(R.string.map_location_settings))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.map_continue_anyway))
            }
        }
    )
}

@Composable
fun NoLocationDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.map_no_location_signal)) },
        text = { Text(stringResource(R.string.map_no_location_signal_info)) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.map_continue_anyway))
            }
        },
        dismissButton = null
    )
}

@Composable
fun OutOfBoundsDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.map_outside_bounds)) },
        text = { Text(stringResource(R.string.map_outside_bounds_info)) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.map_continue_anyway))
            }
        },
        dismissButton = null
    )
}
