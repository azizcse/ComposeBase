package com.technonext.common.design_system.components

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.technonext.common.base.model.MainUiController
import com.technonext.common.base.model.hideTopBar
import com.technonext.common.base.model.showTopBar
import com.technonext.common.design_system.R
import com.technonext.common.design_system.theme.Blue50
import com.technonext.common.design_system.theme.Blue500
import com.technonext.common.design_system.theme.White
import com.technonext.common.design_system.theme.bodyTextLGRegular
import com.technonext.common.design_system.theme.bodyTextLGSemiBold
import com.technonext.common.design_system.theme.displayH4SemiBold
import kotlinx.coroutines.launch

/**
 * Created by Tanvir3488 on 26,November,2024
 */

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionHandel(
    showRationalDialog: Boolean,
    onPermissionChanged: (Boolean, String) -> Unit,
    permissionDone: () -> Unit,
) {
    Log.e("Permission", "Fun Called $showRationalDialog")
    val permissionList = arrayListOf<String>()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissionList.add(Manifest.permission.POST_NOTIFICATIONS)
    }
    permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
    var permissionRequested by remember { mutableStateOf(false) }
    var permissionResponseReceived by remember { mutableStateOf(false) }

    val permissionsState =
        rememberMultiplePermissionsState(permissionList) {
            permissionResponseReceived = true // Set when the dialog result is received
        }

    LaunchedEffect(permissionRequested) {
        if (!permissionRequested) {
            permissionsState.launchMultiplePermissionRequest()
            permissionRequested = true // Mark request as initiated
        }
    }

    permissionsState.permissions.forEachIndexed { index, permission ->
        when {
            !permissionRequested -> {
                Log.d("Permission", "Permission not yet requested.")
            }

            permissionRequested && !permissionResponseReceived -> {
                Log.d("Permission", "Permission dialog is still pending.")
            }

            permission.status.isGranted -> {
                Log.d("Permission", "Permission is granted.")
                onPermissionChanged(true, permission.permission)
                if (index == permissionList.size - 1) {
                    permissionDone()
                    return
                }
            }

            permission.status.shouldShowRationale -> {
                Log.d("Permission", "Permission denied, rationale can be shown.")
                onPermissionChanged(false, permission.permission)
                if (index == permissionList.size - 1) {
                    permissionDone()
                    return
                }
            }

            !permission.status.isGranted && !permission.status.shouldShowRationale -> {
                onPermissionChanged(false, permission.permission)
                if (permission.permission == Manifest.permission.ACCESS_FINE_LOCATION) {
                    if (showRationalDialog) {
                        ShowRational(
                            stringResource(id = R.string.location_permission_title),
                            stringResource(id = R.string.location_permission_description),
                            onSkip = {
                                // Handle skip logic
                            },
                            onGoToSetting = {
                                // Handle navigation to settings
                            },
                        )
                    }
                }
                if (index == permissionList.size - 1) {
                    permissionDone()
                    return
                }
            }
        }
    }
}

@Composable
fun ShowRational(
    title: String,
    description: String,
    onSkip: () -> Unit,
    onGoToSetting: () -> Unit,
) {
    val context = LocalContext.current
    var isDialogVisible by remember { mutableStateOf(true) }

    Log.e("ShowRational", "ShowRational $isDialogVisible")
    if (isDialogVisible) {
        Dialog(
            onDismissRequest = { isDialogVisible = true },
            properties =
                DialogProperties(
                    usePlatformDefaultWidth = false,
                    dismissOnClickOutside = false,
                    dismissOnBackPress = false,
                ),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .background(shape = RoundedCornerShape(16.dp), color = White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Icon(
                    painterResource(id = R.drawable.ic_alert),
                    tint = Color.Unspecified,
                    contentDescription = "ic_alert",
                )
                Text(
                    text = title,
                    modifier =
                        Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                    style = displayH4SemiBold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = description,
                    modifier =
                        Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                    style = bodyTextLGRegular,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                ) {
                    Button(
                        modifier = Modifier.weight(0.45f),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = Blue50, // Background color
                                contentColor = Color.White, // Text color
                            ),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            onSkip()
                            isDialogVisible = false
                        },
                    ) {
                        Text(
                            text = "Skip Now",
                            style = bodyTextLGSemiBold.copy(fontSize = 14.sp),
                            color = Blue500,
                            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        )
                    }
                    Spacer(modifier = Modifier.weight(0.05f))
                    Button(
                        modifier = Modifier.weight(0.45f),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = Blue500, // Background color
                                contentColor = Color.White, // Text color
                            ),
                        contentPadding = PaddingValues(8.dp),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            onGoToSetting()
                            openAppSettings(context)
                            isDialogVisible = false
                        },
                    ) {
                        Text(
                            text = "Go to Settings",
                            style = bodyTextLGSemiBold.copy(fontSize = 14.sp),
                            color = White,
                            modifier = Modifier.padding(vertical = 10.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CheckPermission(navUiController: MutableState<MainUiController>) {
    val missingPermission = arrayListOf<String>()
    val coroutineScope = rememberCoroutineScope()

    PermissionHandel(true, { isPermissionGranted, permissionName ->
        Log.e("Permissiontt", permissionName + " " + isPermissionGranted)
        if (!isPermissionGranted) {
            missingPermission.add(permissionName)
        }
    }, permissionDone = {
        navUiController.value.missingPermissions.clear()
        coroutineScope.launch {
            if (missingPermission.size > 0) {
                navUiController.value = navUiController.value.showTopBar()
                navUiController.value.missingPermissions.addAll(missingPermission)
            } else {
                navUiController.value = navUiController.value.hideTopBar()
            }
        }
    })
}

fun openAppSettings(context: Context) {
    val intent =
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
    context.startActivity(intent)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckStoragePermission(
    showRationalDialog: Boolean,
    onPermissionChanged: (Boolean) -> Unit,
    permissionDone: () -> Unit,
) {
    val permissionToRequest =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES // For API 33+
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    var showRational by remember { mutableStateOf(false) }
    var permissionRequested by remember { mutableStateOf(false) }
    var permissionResponseReceived by remember { mutableStateOf(false) }
    var permissionHandled by remember { mutableStateOf(false) }
    val permissionsState =
        rememberPermissionState(permissionToRequest) {
            permissionResponseReceived = true // Set when the dialog result is received
        }

    LaunchedEffect(permissionRequested) {
        if (!permissionRequested) {
            permissionsState.launchPermissionRequest()
            permissionRequested = true // Mark request as initiated
        }
    }
    if (showRationalDialog && showRational) {
        ShowRational(
            stringResource(id = R.string.gallery_permission_title),
            stringResource(id = R.string.gallery_permission_description),
            onSkip = {
                // Handle skip logic
            },
            onGoToSetting = {
                // Handle navigation to settings
            },
        )
    }
    if (!permissionHandled) {
        permissionsState.let { permission ->
            when {
                !permissionRequested -> {
                    Log.d("Permission", "Permission not yet requested.")
                }

                permissionRequested && !permissionResponseReceived -> {
                    Log.d("Permission", "Permission dialog is still pending.")
                }

                permission.status.isGranted -> {
                    Log.d("Permission", "Permission is granted.")
                    onPermissionChanged(true)
                    permissionDone()
                    permissionHandled = true
                    return
                }

                permission.status.shouldShowRationale -> {
                    Log.d("Permission", "Permission denied, rationale can be shown.")
                    onPermissionChanged(false)
                    permissionDone()
                    permissionHandled = true
                    return
                }

                !permission.status.isGranted && !permission.status.shouldShowRationale -> {
                    onPermissionChanged(false)
                    Log.d("Permission", "Permission denied")
                    showRational = true
                    permissionHandled = true
                    permissionDone()
                    return
                }

                else -> {
                    onPermissionChanged(false)
                    permissionDone()
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckCameraPermission(
    showRationalDialog: Boolean,
    onPermissionChanged: (Boolean) -> Unit,
    permissionDone: () -> Unit,
) {
    val permissionToRequest = Manifest.permission.CAMERA
    var permissionRequested by remember { mutableStateOf(false) }
    var showRational by remember { mutableStateOf(false) }
    var permissionResponseReceived by remember { mutableStateOf(false) }
    var permissionHandled by remember { mutableStateOf(false) } // To prevent redundant executions

    val permissionsState =
        rememberPermissionState(permissionToRequest) {
            permissionResponseReceived = true // Set when dialog result is received
        }

    LaunchedEffect(permissionRequested) {
        if (!permissionRequested) {
            permissionsState.launchPermissionRequest()
            permissionRequested = true // Mark request as initiated
        }
    }
    if (showRationalDialog && showRational) {
        ShowRational(
            stringResource(id = R.string.camera_permission_title),
            stringResource(id = R.string.camera_permission_description),
            onSkip = {
                // Handle skip logic
            },
            onGoToSetting = {
                // Handle navigation to settings
            },
        )
    }

    // Avoid repeated recompositions by guarding with `permissionHandled`
    if (!permissionHandled) {
        permissionsState.let { permission ->
            Log.e("Permission", "permissionsStateWhen")
            when {
                !permissionRequested -> {
                    Log.d("Permission", "Permission not yet requested.")
                }

                permissionRequested && !permissionResponseReceived -> {
                    Log.d("Permission", "Permission dialog is still pending.")
                }

                permission.status.isGranted -> {
                    Log.d("Permission", "Permission is granted.")
                    onPermissionChanged(true)
                    permissionDone()
                    permissionHandled = true // Mark as handled
                    return
                }

                permission.status.shouldShowRationale -> {
                    Log.d("Permission", "Permission denied, rationale can be shown.")
                    onPermissionChanged(false)
                    permissionDone()
                    permissionHandled = true // Mark as handled
                    return
                }

                !permission.status.isGranted && !permission.status.shouldShowRationale -> {
                    onPermissionChanged(false)

                    showRational = true
                    permissionDone()
                    permissionHandled = true // Mark as handled
                    return
                }

                else -> {
                    onPermissionChanged(false)
                    permissionDone()
                    permissionHandled = true // Mark as handled
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckContactPermission(
    showRationalDialog: Boolean,
    onPermissionChanged: (Boolean) -> Unit,
    permissionDone: () -> Unit,
) {
    val permissionToRequest = Manifest.permission.READ_CONTACTS
    var permissionRequested by remember { mutableStateOf(false) }
    var showRational by remember { mutableStateOf(false) }
    var permissionResponseReceived by remember { mutableStateOf(false) }
    var permissionHandled by remember { mutableStateOf(false) } // To prevent redundant executions

    val permissionsState =
        rememberPermissionState(permissionToRequest) {
            permissionResponseReceived = true // Set when dialog result is received
        }

    LaunchedEffect(permissionRequested) {
        if (!permissionRequested) {
            permissionsState.launchPermissionRequest()
            permissionRequested = true // Mark request as initiated
        }
    }
    if (showRationalDialog && showRational) {
        ShowRational(
            stringResource(id = R.string.contacts_permission_title),
            stringResource(id = R.string.contacts_permission_description),
            onSkip = {
                // Handle skip logic
            },
            onGoToSetting = {
                // Handle navigation to settings
            },
        )
    }

    // Avoid repeated recompositions by guarding with `permissionHandled`
    if (!permissionHandled) {
        permissionsState.let { permission ->
            Log.e("Permission", "permissionsStateWhen")
            when {
                !permissionRequested -> {
                    Log.d("Permission", "Permission not yet requested.")
                }

                permissionRequested && !permissionResponseReceived -> {
                    Log.d("Permission", "Permission dialog is still pending.")
                }

                permission.status.isGranted -> {
                    Log.d("Permission", "Permission is granted.")
                    onPermissionChanged(true)
                    permissionDone()
                    permissionHandled = true // Mark as handled
                    return
                }

                permission.status.shouldShowRationale -> {
                    Log.d("Permission", "Permission denied, rationale can be shown.")
                    onPermissionChanged(false)
                    permissionDone()
                    permissionHandled = true // Mark as handled
                    return
                }

                !permission.status.isGranted && !permission.status.shouldShowRationale -> {
                    onPermissionChanged(false)

                    showRational = true
                    permissionDone()
                    permissionHandled = true // Mark as handled
                    return
                }

                else -> {
                    onPermissionChanged(false)
                    permissionDone()
                    permissionHandled = true // Mark as handled
                }
            }
        }
    }
}
