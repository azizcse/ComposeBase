package com.dawinder.btnjc.ui.composables.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.aregyan.compose.R
import com.aregyan.compose.ui.theme.md_theme_light_inversePrimary

/**
 * Composable function that represents the list screen of the application.
 */
@Composable
fun ListScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(id = R.string.list),
            style = typography.titleLarge,
            color = md_theme_light_inversePrimary
        )
    }
}