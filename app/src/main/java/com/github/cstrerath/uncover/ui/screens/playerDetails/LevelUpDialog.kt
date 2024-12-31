package com.github.cstrerath.uncover.ui.screens.playerDetails

import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.github.cstrerath.uncover.R

private const val TAG = "LevelUpDialog"

@Composable
internal fun LevelUpDialog(
    message: String,
    onDismiss: () -> Unit
) {
    Log.d(TAG, "Showing level up dialog with message: $message")

    AlertDialog(
        onDismissRequest = {
            Log.d(TAG, "Dialog dismissed via outside click")
            onDismiss()
        },
        title = { DialogTitle() },
        text = { DialogMessage(message) },
        confirmButton = {
            DialogConfirmButton(onDismiss)
        }
    )
}

@Composable
private fun DialogTitle() {
    Text(text = stringResource(R.string.level_up_information))
}

@Composable
private fun DialogMessage(message: String) {
    Text(message)
}

@Composable
private fun DialogConfirmButton(onDismiss: () -> Unit) {
    Button(
        onClick = {
            Log.d(TAG, "Dialog confirmed")
            onDismiss()
        }
    ) {
        Text(stringResource(R.string.button_ok))
    }
}
