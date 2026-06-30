package com.example.rescatando_mascotas_forever.presentation.suscripciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import com.example.rescatando_mascotas_forever.data.network.models.Suscripcion
import com.example.rescatando_mascotas_forever.data.repository.SuscripcionRepository
import com.google.gson.JsonPrimitive
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

    private val _createState = MutableStateFlow<Result<Suscripcion>?>(null)
    val createState: StateFlow<Result<Suscripcion>?> = _createState

    init {
        loadSuscripciones()
    }

    fun createSuscripcion(data: Map<String, Any>) {
        viewModelScope.launch {
            repository.createSuscripcion(data).collect {
                _createState.value = it
                if (it.isSuccess) loadSuscripciones()
            }
        }
    }

    fun resetCreateState() {
        _createState.value = null
    }

    fun retry() {
        loadSuscripciones()
    }

    fun loadSuscripciones() {
        _state.value = SuscripcionState.Loading
        viewModelScope.launch {
            repository.getMisSuscripciones().collect { result ->
                result.onSuccess { list ->
                    if (list.isEmpty()) {
                        _state.value = SuscripcionState.Success(emptyList())
                    } else {
                        _state.value = SuscripcionState.Success(list)
                    }
                }.onFailure {
                    _state.value = SuscripcionState.Error(it.message ?: "Error desconocido")
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

    // --- MÉTODOS DE ADMINISTRADOR ---
    fun loadAllSuscripciones() {
        _state.value = SuscripcionState.Loading
        viewModelScope.launch {
            repository.getAllSuscripciones().collect { result ->
                result.onSuccess { list ->
                    _state.value = SuscripcionState.Success(list)
                }.onFailure {
                    _state.value = SuscripcionState.Error(it.message ?: "Error al obtener suscripciones")
                }
            }
        }
    }

    private fun getMockSuscripciones(): List<Suscripcion> {
        return listOf(
            Suscripcion(
                id = 1,
                userId = JsonPrimitive(1),
                mascotaId = JsonPrimitive(1),
                montoMensual = JsonPrimitive(25000.0),
                frecuencia = "mensual",
                fechaInicio = "2024-03-15",
                mensajeApoyo = "¡Eres un campeón! Recupérate pronto para que encuentres un hogar.",
                estado = "activo"
            )
        )
    }

    fun updateStatus(id: Int, newStatus: String) {
        viewModelScope.launch {
            repository.updateSuscripcionStatus(id, newStatus).collect { result ->
                result.onSuccess {
                    loadAllSuscripciones()
                }
            }
        }
    }
}
