package com.example.rescatando_mascotas_forever.presentation.eventos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.api.RetrofitClient
import com.example.rescatando_mascotas_forever.data.network.models.Evento
import com.example.rescatando_mascotas_forever.data.repository.EventoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed class EventoState {
    object Loading : EventoState()
    data class Success(val eventos: List<Evento>) : EventoState()
    data class Error(val message: String) : EventoState()
}

class EventoViewModel(
    private val repository: EventoRepository = EventoRepository(RetrofitClient.eventoApi)
) : ViewModel() {

    private val _state = MutableStateFlow<EventoState>(EventoState.Loading)
    val state: StateFlow<EventoState> = _state.asStateFlow()

    private val _eventoDetalle = MutableStateFlow<Evento?>(null)
    val eventoDetalle: StateFlow<Evento?> = _eventoDetalle

    init {
        getEventos()
    }

    fun getEventos() {
        viewModelScope.launch {
            _state.value = EventoState.Loading
            repository.getEventos().collect { result ->
                result.onSuccess {
                    _state.value = EventoState.Success(it)
                }.onFailure {
                    _state.value = EventoState.Error(it.message ?: "Error desconocido")
                }
            }
        }
    }

    fun getEventoById(id: Int) {
        // 1. Intento rápido: si ya tenemos los eventos, lo asignamos de inmediato
        val currentState = _state.value
        if (currentState is EventoState.Success) {
            _eventoDetalle.value = currentState.eventos.find { it.id == id }
        }

        // 2. Por si acaso aún está cargando la lista, nos quedamos escuchando
        viewModelScope.launch {
            state.collectLatest { latestState ->
                if (latestState is EventoState.Success) {
                    _eventoDetalle.value = latestState.eventos.find { it.id == id }
                }
            }
        }
    }
    
    fun limpiarDetalle() {
        _eventoDetalle.value = null
    }
}
