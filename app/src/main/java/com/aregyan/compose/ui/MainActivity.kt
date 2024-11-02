package com.aregyan.compose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.aregyan.compose.ui.navgraph.NavGraph
import com.aregyan.compose.ui.theme.BaseAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition(condition = { viewModel.loading.value })
        }
        setContent {
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
                    NavGraph(mainViewModel = viewModel)
                }
            }
        }
    }
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