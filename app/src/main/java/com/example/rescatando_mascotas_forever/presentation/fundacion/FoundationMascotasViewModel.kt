package com.example.rescatando_mascotas_forever.presentation.fundacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import com.example.rescatando_mascotas_forever.data.repository.FoundationMascotasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class FoundationMascotasState {
    object Idle : FoundationMascotasState()
    object Loading : FoundationMascotasState()
    data class Success(val mascotas: List<Mascota>, val currentPage: Int, val lastPage: Int) : FoundationMascotasState()
    data class Error(val message: String) : FoundationMascotasState()
}

class FoundationMascotasViewModel(
    private val repository: FoundationMascotasRepository = FoundationMascotasRepository(RetrofitClient.entityApi)
) : ViewModel() {

    private val _state = MutableStateFlow<FoundationMascotasState>(FoundationMascotasState.Idle)
    val state: StateFlow<FoundationMascotasState> = _state.asStateFlow()

    init {
        loadMascotas()
    }

    fun loadMascotas(page: Int = 1, search: String? = null) {
        viewModelScope.launch {
            _state.value = FoundationMascotasState.Loading
            try {
                val response = repository.getMascotas(page, search)
                if (response.success && response.data != null) {
                    _state.value = FoundationMascotasState.Success(
                        mascotas = response.data.data,
                        currentPage = response.data.currentPage,
                        lastPage = response.data.lastPage
                    )
                } else {
                    _state.value = FoundationMascotasState.Error(response.message ?: "Error al cargar mascotas")
                }
            } catch (e: Exception) {
                _state.value = FoundationMascotasState.Error(e.message ?: "Error de conexión")
            }
        }
    }

    fun deleteMascota(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.eliminarMascota(id)
                if (response.success) {
                    loadMascotas() // Recargar lista tras eliminar
                } else {
                    _state.value = FoundationMascotasState.Error(response.message ?: "No se pudo eliminar la mascota")
                }
            } catch (e: Exception) {
                _state.value = FoundationMascotasState.Error("Error al intentar eliminar: ${e.message}")
            }
        }
    }
}
