package com.example.rescatando_mascotas_forever.presentation.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.api.AdminStatsResponse
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AdminHomeState {
    object Loading : AdminHomeState()
    data class Success(val stats: AdminStatsResponse) : AdminHomeState()
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
                // Intentamos obtener datos reales del servidor en Railway
                val response = RetrofitClient.adminApi.getGeneralStats()
                
                if (response.success && response.data != null) {
                    _uiState.value = AdminHomeState.Success(response.data)
                    Log.d("AdminHomeViewModel", "Datos cargados exitosamente: ${response.data}")
                } else {
                    _uiState.value = AdminHomeState.Error(response.message ?: "Error al obtener estadísticas")
                    Log.e("AdminHomeViewModel", "Respuesta fallida: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("AdminHomeViewModel", "Error cargando datos de Railway: ${e.message}", e)
                _uiState.value = AdminHomeState.Error(
                    e.localizedMessage ?: "Error de conexión con el servidor de Railway"
                )
            }
        }
    }
}
