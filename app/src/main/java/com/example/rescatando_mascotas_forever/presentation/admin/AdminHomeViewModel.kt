package com.example.rescatando_mascotas_forever.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.api.AdminStatsResponse
import com.example.rescatando_mascotas_forever.data.network.api.ReporteItem
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AdminHomeState {
    object Loading : AdminHomeState()
    data class Success(
        val stats: AdminStatsResponse,
        val reportesRecientes: List<ReporteItem>
    ) : AdminHomeState()
    data class Error(val message: String) : AdminHomeState()
}

class AdminHomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<AdminHomeState>(AdminHomeState.Loading)
    val uiState: StateFlow<AdminHomeState> = _uiState.asStateFlow()

    init {
        fetchDashboardData()
    }

    fun fetchDashboardData() {
        viewModelScope.launch {
            _uiState.value = AdminHomeState.Loading
            try {
                val stats = RetrofitClient.adminApi.getGeneralStats()
                val reportes = RetrofitClient.adminApi.getReportesPlataforma(null)
                
                _uiState.value = AdminHomeState.Success(stats, reportes)
            } catch (e: Exception) {
                // Fallback con datos de ejemplo en caso de error o desarrollo
                _uiState.value = AdminHomeState.Success(
                    stats = AdminStatsResponse(
                        totalMascotas = 24, 
                        totalRescates = 12, 
                        totalAdopciones = 8, 
                        totalUsuarios = 150
                    ),
                    reportesRecientes = emptyList()
                )
            }
        }
    }
}
