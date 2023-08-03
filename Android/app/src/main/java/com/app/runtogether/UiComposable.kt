package com.app.runtogether


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState

@Composable
internal fun AlertDialogComposable(
    applicationContext: Context,
    showAlertDialog: MutableState<Boolean>
) {
    AlertDialog(
        onDismissRequest = {
            showAlertDialog.value = false
        },
        title = {
            Text(text = "GPS disabled")
        },
        text = {
            Text(text = "GPS is turned off but is needed to get the coordinates")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    if (intent.resolveActivity(applicationContext.packageManager) != null) {
                        applicationContext.startActivity(intent)
                    }
                    showAlertDialog.value = false
                }
            ) {
                Text("Turned on the GPS")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { showAlertDialog.value = false  }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
internal fun SnackBarComposable(
    snackbarHostState: SnackbarHostState,
    applicationContext: Context,
    showSnackBar: MutableState<Boolean>
) {
    LaunchedEffect(snackbarHostState) {
        val result = snackbarHostState.showSnackbar(
            message = "Permission are needed to get your position",
            actionLabel = "Go to settings"
        )
        when (result) {
            SnackbarResult.ActionPerformed -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", applicationContext.packageName, null)
                }
                if (intent.resolveActivity(applicationContext.packageManager) != null) {
                    applicationContext.startActivity(intent)
                }
            }
            SnackbarResult.Dismissed -> {
                showSnackBar.value = false
            }
        }
    }
}
