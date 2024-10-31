package com.aregyan.compose.ui.permission

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aregyan.compose.util.HandleOneEvent

/**
 * @author md-azizul-islam
 * Created 10/31/24 at 10:19 AM
 */


@Composable
fun PermissionScreen(modifier: Modifier = Modifier) {
    val viewModel = viewModel(modelClass = PermissionViewModel::class)
    val context = LocalContext.current
    val activity = context as Activity
    val permissionState by viewModel.permissionState.collectAsState()

    HandleOneEvent(viewModel.events) {  events ->
        when (events) {
            Events.SendToSettings -> context.openAppSettings()
            Events.ShowGrantedText -> viewModel.updatePermissionState(PermissionState.GRANTED)
            Events.ShowPermissionRationale -> viewModel.updatePermissionState(
                PermissionState.SHOULD_SHOW_PERMISSION_RATIONALE
            )
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            val shouldShowPermissionRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.CAMERA
            )

            viewModel.updatePermissionEvent(isGranted, shouldShowPermissionRationale)
        }
    )
    viewModel.isPermissionGranted(context)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp)
    ) {

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }
            ) {
                Text(
                    text = "Camera permission"
                )
            }
        }

        when (permissionState) {
            PermissionState.GRANTED -> Text(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = "Permission Accepted"
            )

            PermissionState.SHOULD_SHOW_PERMISSION_RATIONALE -> PermissionDialog(
                onDismiss = { viewModel.updatePermissionState(PermissionState.NO_ACTION) },
                onConfirmation = {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                    viewModel.updatePermissionState(PermissionState.NO_ACTION)
                }
            )

            PermissionState.NO_ACTION -> Unit
        }
    }
}

private fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}
