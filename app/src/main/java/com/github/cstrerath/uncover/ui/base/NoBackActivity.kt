package com.github.cstrerath.uncover.ui.base

import android.os.Bundle
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
import com.github.cstrerath.uncover.ui.theme.UncoverTheme

abstract class NoBackActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var showExitDialog by remember { mutableStateOf(false) }

            BackHandler {
                showExitDialog = true
            }

            UncoverTheme {
                if (showExitDialog) {
                    ExitConfirmationDialog(
                        onDismiss = { showExitDialog = false },
                        onConfirm = { finishAffinity() }
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
}
