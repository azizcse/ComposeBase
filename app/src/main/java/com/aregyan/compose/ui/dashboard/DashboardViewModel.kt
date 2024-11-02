package com.aregyan.compose.ui.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class DashboardViewModel:ViewModel() {
    var dashboardUiState by mutableStateOf(DashboardUIState())
        private set
}