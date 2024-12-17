package com.aregyan.compose.ui

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.aregyan.compose.ui.navgraph.NavGraph
import com.aregyan.compose.ui.theme.BaseAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition(condition = { viewModel.loading.value })
        }

        val connectivityManager =
            getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        setContent {
            val isConnected = remember { mutableStateOf(true) }
            LaunchedEffect(Unit) {
                lifecycleScope.launch {
                    monitorNetworkConnectivity(connectivityManager, isConnected)
                }
            }
            BaseAppTheme {
                val isSystemInDarkMode = isSystemInDarkTheme()
                val systemUiColor = rememberSystemUiController()
                SideEffect {
                    systemUiColor.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = !isSystemInDarkMode
                    )
                }
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                ) {
                    MainApp(isConnected, viewModel)
                    //NavGraph(mainViewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MainApp(isConnected: MutableState<Boolean>, mainViewModel: MainViewModel) {
    if (!isConnected.value) {
        NoInternetDialog()
    } else {
        NavGraph(mainViewModel = mainViewModel)
    }
}


@RequiresApi(Build.VERSION_CODES.N)
fun monitorNetworkConnectivity(
    connectivityManager: ConnectivityManager,
    isConnected: MutableState<Boolean>
) {
    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            isConnected.value = true
        }

        override fun onLost(network: Network) {
            isConnected.value = false
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            val hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            isConnected.value = hasInternet
        }
    }

    val activeNetwork = connectivityManager.activeNetwork
    val activeNetworkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
    isConnected.value = activeNetworkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

    connectivityManager.registerDefaultNetworkCallback(networkCallback)
}


@Composable
fun NoInternetDialog() {
    AlertDialog(
        onDismissRequest = { /* Dialog dismissed */ },
        title = { Text(text = "No Internet") },
        text = { Text(text = "You are not connected to the internet. Please check your connection.") },
        confirmButton = {
            Button(onClick = { /* Retry or dismiss logic */ }) {
                Text(text = "Retry")
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BadgeDisplay(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        BadgedBox(badge = { Badge { Text("99+") } }) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Badge",
                modifier = Modifier.size(50.dp, 50.dp),
                tint = Color.Green
            )
        }
    }
}


@Composable
private fun MaterialSwitch() {
    var isChecked by remember { mutableStateOf(false) }
    val icon = if (isChecked) Icons.Filled.Check else Icons.Filled.Clear

    val icon2: (@Composable () -> Unit)? = if (isChecked) {
        {
            Icon(
                imageVector = icon,
                contentDescription = "",
                modifier = Modifier.size(SwitchDefaults.IconSize)
            )
        }

    } else null

    Switch(checked = isChecked, onCheckedChange = { isChecked = it }, thumbContent = icon2)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MaterialSlider() {
    var slideValue by remember { mutableStateOf(0f) }
    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(slideValue.toString())
        Slider(
            value = slideValue,
            onValueChange = { slideValue = it },
            valueRange = 0f..5f,
            steps = 5,
            onValueChangeFinished = {

            },
            thumb = {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "",
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                    tint = Color.Red
                )
            }
        )
    }
}

@Composable
private fun MaterialCheckbox() {
    val options = listOf("Bangla", "English", "Urdu")
    var selected by remember { mutableStateOf(options[0]) }
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        options.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .selectable(
                        selected = selected == item,
                        onClick = { selected = item }
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = selected == item, onClick = { selected = item })
                Text(item)
            }
        }
    }
}

@Composable
private fun OpenDialog() {
    var showDialog by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = { showDialog = true }) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = "")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Alert dialog title") },
            text = { Text("Alert dialog description") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                }) {
                    Text("OK")

                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Calcel")

                }
            },
            shape = RoundedCornerShape(10.dp)

        )
    }
}

@Composable
private fun MaterialMenu3() {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "")
        }
    }

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        DropdownMenuItem(
            text = { Text("Edit Profile") },
            onClick = { expanded = false },
            leadingIcon = { Icon(imageVector = Icons.Default.Edit, contentDescription = "") }
        )
        DropdownMenuItem(
            text = { Text("Setting") },
            onClick = { expanded = false },
            leadingIcon = { Icon(imageVector = Icons.Default.Settings, contentDescription = "") }
        )
        Divider()
        DropdownMenuItem(
            text = { Text("Send feedback") },
            onClick = { expanded = false },
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "") },
            trailingIcon = { Text("F5") }
        )
    }
}


@Composable
fun PageIndicatorSample() {
    val numberOfPages = 3
    val (selectedPage, setSelectedPage) = remember {
        mutableStateOf(0)
    }

    // NEVER use this, this is just for example
    LaunchedEffect(
        key1 = selectedPage,
    ) {
        delay(3000)
        setSelectedPage((selectedPage + 1) % numberOfPages)
    }

    PageIndicator(
        numberOfPages = numberOfPages,
        selectedPage = selectedPage,
        defaultRadius = 60.dp,
        selectedLength = 120.dp,
        space = 30.dp,
        animationDurationInMillis = 1000,
    )
}


@Composable
fun PageIndicator(
    numberOfPages: Int,
    modifier: Modifier = Modifier,
    selectedPage: Int = 0,
    selectedColor: Color = Color(0xFF3E6383),
    defaultColor: Color = Color.LightGray,
    defaultRadius: Dp = 20.dp,
    selectedLength: Dp = 60.dp,
    space: Dp = 30.dp,
    animationDurationInMillis: Int = 300,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space),
        modifier = modifier,
    ) {
        for (i in 0 until numberOfPages) {
            val isSelected = i == selectedPage
            PageIndicatorView(
                isSelected = isSelected,
                selectedColor = selectedColor,
                defaultColor = defaultColor,
                defaultRadius = defaultRadius,
                selectedLength = selectedLength,
                animationDurationInMillis = animationDurationInMillis,
            )
        }
    }
}

@Composable
fun PageIndicatorView(
    isSelected: Boolean,
    selectedColor: Color,
    defaultColor: Color,
    defaultRadius: Dp,
    selectedLength: Dp,
    animationDurationInMillis: Int,
    modifier: Modifier = Modifier,
) {
    val color: Color by animateColorAsState(
        targetValue = if (isSelected) {
            selectedColor
        } else {
            defaultColor
        },
        animationSpec = tween(
            durationMillis = animationDurationInMillis,
        )
    )
    val width: Dp by animateDpAsState(
        targetValue = if (isSelected) {
            selectedLength
        } else {
            defaultRadius
        },
        animationSpec = tween(
            durationMillis = animationDurationInMillis,
        )
    )

    Canvas(
        modifier = modifier
            .size(
                width = width,
                height = defaultRadius,
            ),
    ) {
        drawRoundRect(
            color = color,
            topLeft = Offset.Zero,
            size = Size(
                width = width.toPx(),
                height = defaultRadius.toPx(),
            ),
            cornerRadius = CornerRadius(
                x = defaultRadius.toPx(),
                y = defaultRadius.toPx(),
            ),
        )
    }
}