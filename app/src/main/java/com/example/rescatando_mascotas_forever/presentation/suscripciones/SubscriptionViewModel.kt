package com.example.rescatando_mascotas_forever.presentation.suscripciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import com.example.rescatando_mascotas_forever.data.network.models.Suscripcion
import com.example.rescatando_mascotas_forever.data.repository.SuscripcionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class SuscripcionState {
    object Loading : SuscripcionState()
    data class Success(val suscripciones: List<Suscripcion>) : SuscripcionState()
    data class Error(val message: String) : SuscripcionState()
}

class SubscriptionViewModel(
    private val repository: SuscripcionRepository = SuscripcionRepository(RetrofitClient.suscripcionApi)
) : ViewModel() {

    private val _state = MutableStateFlow<SuscripcionState>(SuscripcionState.Loading)
    val state: StateFlow<SuscripcionState> = _state

    init {
        loadSuscripciones()
    }

    fun retry() {
        loadSuscripciones()
    }

    fun loadSuscripciones() {
        _state.value = SuscripcionState.Loading
        viewModelScope.launch {
            repository.getMisSuscripciones().collect { result ->
                result.onSuccess {
                    _state.value = SuscripcionState.Success(it)
                }.onFailure {
                    _state.value = SuscripcionState.Error(it.message ?: "Error al cargar suscripciones")
                }
            }
        }
    }

    fun cancelarSuscripcion(id: Int) {
        viewModelScope.launch {
            repository.deleteSuscripcion(id).collect { result ->
                result.onSuccess {
                    loadSuscripciones() // Recargar lista
                }
            }
        }
    }
}
