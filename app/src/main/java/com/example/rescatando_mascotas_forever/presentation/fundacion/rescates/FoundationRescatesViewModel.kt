package com.example.rescatando_mascotas_forever.presentation.fundacion.rescates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.models.Rescate
import com.example.rescatando_mascotas_forever.data.repository.FoundationRescatesRepository
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class FoundationRescatesState {
    object Loading : FoundationRescatesState()
    data class Success(val rescates: List<Rescate>) : FoundationRescatesState()
    data class Error(val message: String) : FoundationRescatesState()
}

sealed class FoundationRescateDetailState {
    object Loading : FoundationRescateDetailState()
    data class Success(val rescate: Rescate) : FoundationRescateDetailState()
    data class Error(val message: String) : FoundationRescateDetailState()
}

class FoundationRescatesViewModel(
    private val repository: FoundationRescatesRepository = FoundationRescatesRepository(RetrofitClient.entityApi)
) : ViewModel() {
    
    private val _state = MutableStateFlow<FoundationRescatesState>(FoundationRescatesState.Loading)
    val state: StateFlow<FoundationRescatesState> = _state

    private val _detailState = MutableStateFlow<FoundationRescateDetailState>(FoundationRescateDetailState.Loading)
    val detailState: StateFlow<FoundationRescateDetailState> = _detailState

    fun fetchRescates(tipo: Int) {
        viewModelScope.launch {
            _state.value = FoundationRescatesState.Loading
            try {
                val response = if (tipo == 0) {
                    repository.getRescatesDisponibles()
                } else {
                    repository.getMisRescates()
                }

                if (response.success) {
                    val list = response.data?.data ?: emptyList()
                    _state.value = FoundationRescatesState.Success(list)
                } else {
                    _state.value = FoundationRescatesState.Error(response.message ?: "Error al cargar")
                }
            } catch (e: Exception) {
                _state.value = FoundationRescatesState.Error(e.message ?: "Error de conexión")
            }
        }
    }

    fun fetchRescateById(id: Int) {
        viewModelScope.launch {
            _detailState.value = FoundationRescateDetailState.Loading
            try {
                val response = repository.getRescateById(id)
                if (response.success && response.data != null) {
                    _detailState.value = FoundationRescateDetailState.Success(response.data)
                } else {
                    _detailState.value = FoundationRescateDetailState.Error(response.message ?: "No se encontró el rescate")
                }
            } catch (e: Exception) {
                _detailState.value = FoundationRescateDetailState.Error(e.message ?: "Error de conexión")
            }
        }
    }

    fun aceptarRescate(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.aceptarRescate(id)
                if (response.success) {
                    onSuccess()
                }
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    fun rechazarRescate(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.rechazarRescate(id)
                if (response.success) {
                    onSuccess()
                }
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
}
