package com.example.rescatando_mascotas_forever.presentation.adopciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class MascotaDetalleState {
    object Loading : MascotaDetalleState()
    data class Success(val mascota: Mascota) : MascotaDetalleState()
    data class Error(val message: String) : MascotaDetalleState()
}

class MascotaDetalleViewModel : ViewModel() {
    private val _state = MutableStateFlow<MascotaDetalleState>(MascotaDetalleState.Loading)
    val state: StateFlow<MascotaDetalleState> = _state

    fun cargarMascota(id: Int) {
        viewModelScope.launch {
            _state.value = MascotaDetalleState.Loading
            try {
                val response = RetrofitClient.mascotaApi.getMascotaById(id)
                if (response.success && response.data != null) {
                    // Ahora usamos el objeto mascota directamente desde response.data
                    _state.value = MascotaDetalleState.Success(response.data)
                } else {
                    _state.value = MascotaDetalleState.Error(response.message ?: "No se pudo cargar la mascota")
                }
            } catch (e: Exception) {
                _state.value = MascotaDetalleState.Error("Error: ${e.message}")
            }
        }
    }
}
