package com.example.rescatando_mascotas_forever.presentation.adopciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.api.RetrofitClient
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdopcionViewModel : ViewModel() {

    private val _mascotas = MutableStateFlow<List<Mascota>>(emptyList())
    val mascotas: StateFlow<List<Mascota>> = _mascotas

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        cargarMascotas()
    }

    fun cargarMascotas(especie: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                // Filtramos por estado "En adopcion" según tu requerimiento
                val response = RetrofitClient.mascotaApi.getMascotas(especie = especie, estado = "En adopcion")
                if (response.success) {
                    _mascotas.value = response.data.data
                } else {
                    _error.value = response.message ?: "Error al obtener mascotas para adopción"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.localizedMessage}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
