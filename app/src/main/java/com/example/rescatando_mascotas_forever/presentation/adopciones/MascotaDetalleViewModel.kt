package com.example.rescatando_mascotas_forever.presentation.adopciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.google.gson.Gson
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
                    val gson = Gson()
                    val json = gson.toJson(response.data)
                    val mascota = gson.fromJson(json, Mascota::class.java)
                    _state.value = MascotaDetalleState.Success(mascota)
                } else {
                    _state.value = MascotaDetalleState.Error(response.message ?: "No se encontró la mascota")
                }
            } catch (e: Exception) {
                _state.value = MascotaDetalleState.Error(e.message ?: "Error al cargar los detalles")
            }
        }
    }
}
