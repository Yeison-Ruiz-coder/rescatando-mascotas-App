package com.example.rescatando_mascotas_forever.presentation.adopciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
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

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    private val _lastPage = MutableStateFlow(1)
    val lastPage: StateFlow<Int> = _lastPage

    private val _totalMascotas = MutableStateFlow(0)
    val totalMascotas: StateFlow<Int> = _totalMascotas

    init {
        cargarMascotas()
    }

    fun cargarMascotas(especie: String? = null, page: Int = 1) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _currentPage.value = page
            
            try {
                // Quitamos el filtro de estado explícito por si el backend usa otro término
                val response = RetrofitClient.mascotaApi.getMascotas(especie = especie)
                if (response.success) {
                    // Ahora que el modelo está tipado, el acceso es directo
                    _mascotas.value = response.data?.data ?: emptyList()
                } else {
                    _mascotas.value = emptyList()
                    if (!response.success) {
                        _error.value = response.message ?: "Error al obtener mascotas"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun nextPage(especie: String? = null) {
        if (_currentPage.value < _lastPage.value) {
            cargarMascotas(especie, _currentPage.value + 1)
        }
    }

    fun prevPage(especie: String? = null) {
        if (_currentPage.value > 1) {
            cargarMascotas(especie, _currentPage.value - 1)
        }
    }
}
