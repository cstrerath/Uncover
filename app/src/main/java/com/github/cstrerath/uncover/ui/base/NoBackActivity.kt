package com.github.cstrerath.uncover.ui.base

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.github.cstrerath.uncover.R
import com.github.cstrerath.uncover.ui.screens.UncoverBaseScreen
import com.github.cstrerath.uncover.ui.theme.UncoverTheme

abstract class NoBackActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Creating NoBackActivity")
        setContent {
            SetupScreen()
        }
    }

    @Composable
    private fun SetupScreen() {
        var showExitDialog by remember { mutableStateOf(false) }

        BackHandler {
            Log.d(TAG, "Back button pressed, showing exit dialog")
            showExitDialog = true
        }

        UncoverTheme {
            UncoverBaseScreen {
                if (showExitDialog) {
                    ExitConfirmationDialog(
                        onDismiss = {
                            Log.d(TAG, "Exit dialog dismissed")
                            showExitDialog = false
                        },
                        onConfirm = {
                            Log.d(TAG, "Exit confirmed, finishing activity")
                            finishAffinity()
                        }
                    )
                }
                NoBackContent()
            }
        }
    }

    @Composable
    private fun ExitConfirmationDialog(
        onDismiss: () -> Unit,
        onConfirm: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(R.string.no_back_dialog_header)) },
            text = { Text(stringResource(R.string.no_back_dialog_message)) },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(stringResource(R.string.no_back_dialog_accept))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.no_back_dialog_dismiss))
                }
            }
        )
    }

    @Composable
    abstract fun NoBackContent()

    companion object {
        private const val TAG = "NoBackActivity"
    }
}
