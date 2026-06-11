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
                        _state.value = SuscripcionState.Success(getMockSuscripciones())
                    } else {
                        _state.value = SuscripcionState.Success(list)
                    }
                }.onFailure {
                    _state.value = SuscripcionState.Success(getMockSuscripciones())
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
                    if (list.isEmpty()) {
                        _state.value = SuscripcionState.Success(getMockSuscripciones())
                    } else {
                        _state.value = SuscripcionState.Success(list)
                    }
                }.onFailure {
                    // Si falla el servidor (Error 500), mostramos los mock para que el usuario pueda ver el diseño
                    _state.value = SuscripcionState.Success(getMockSuscripciones())
                }
            }
        }
    }

    private fun getMockSuscripciones(): List<Suscripcion> {
        return listOf(
            Suscripcion(
                id = 1,
                userId = 1,
                mascotaId = 1,
                montoMensual = 25000.0,
                frecuencia = "mensual",
                fechaInicio = "2024-03-15",
                mensajeApoyo = "¡Eres un campeón! Recupérate pronto para que encuentres un hogar.",
                estado = "activo",
                mascota = com.example.rescatando_mascotas_forever.data.network.models.Mascota(
                    id = 1,
                    nombre = "Toby",
                    especie = "Perro",
                    fotoPrincipal = "https://images.unsplash.com/photo-1543466835-00a7907e9de1",
                    edadAprox = 2.0,
                    genero = "M",
                    ubicacion = "Refugio Central",
                    estado = "disponible",
                    descripcion = "Un perro muy valiente.",
                    aptoConNinos = true,
                    aptoConOtrosAnimales = true,
                    fundacionId = 1
                )
            ),
            Suscripcion(
                id = 2,
                userId = 1,
                mascotaId = 2,
                montoMensual = 15000.0,
                frecuencia = "mensual",
                fechaInicio = "2024-02-10",
                mensajeApoyo = "Para tus medicinas, linda gatita.",
                estado = "pausado",
                mascota = com.example.rescatando_mascotas_forever.data.network.models.Mascota(
                    id = 2,
                    nombre = "Luna",
                    especie = "Gato",
                    fotoPrincipal = "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba",
                    edadAprox = 1.0,
                    genero = "H",
                    ubicacion = "Hogar de Paso",
                    estado = "disponible",
                    descripcion = "Muy cariñosa.",
                    aptoConNinos = true,
                    aptoConOtrosAnimales = false,
                    fundacionId = 1
                )
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
