package com.example.rescatando_mascotas_forever.presentation.eventos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import com.example.rescatando_mascotas_forever.data.network.models.Evento
import com.example.rescatando_mascotas_forever.data.repository.EventoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class EventoState {
    object Loading : EventoState()
    data class Success(val eventos: List<Evento>) : EventoState()
    data class Error(val message: String) : EventoState()
}

class EventoViewModel(
    private val repository: EventoRepository = EventoRepository(
        com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient.eventoApi
    )
) : ViewModel() {

    private val _state = MutableStateFlow<EventoState>(EventoState.Loading)
    val state: StateFlow<EventoState> = _state.asStateFlow()

    private val _eventoDetalle = MutableStateFlow<Evento?>(null)
    val eventoDetalle: StateFlow<Evento?> = _eventoDetalle

    // Nuevos estados para filtros
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Todos")
    val selectedCategory = _selectedCategory.asStateFlow()

    // Flow que combina el estado de los eventos con los filtros
    val filteredEventos: StateFlow<List<Evento>> = combine(
        _state,
        _searchText,
        _selectedCategory
    ) { state, query, category ->
        if (state is EventoState.Success) {
            state.eventos.filter { evento ->
                // Si la categoría es "Todos", aceptamos cualquier evento aunque el tipo sea null
                val matchesCat = if (category == "Todos" || category == "All") {
                    true
                } else {
                    evento.tipo?.contains(category, ignoreCase = true) == true
                }

                val matchesSearch = query.isEmpty() ||
                        evento.nombre.contains(query, ignoreCase = true) ||
                        evento.lugar.contains(query, ignoreCase = true)
                
                matchesCat && matchesSearch
            }
        } else {
            emptyList()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        getEventos()
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }

    fun getEventos() {
        viewModelScope.launch {
            _state.value = EventoState.Loading
            repository.getEventos().collect { result ->
                result.onSuccess { eventos ->
                    println("DEBUG_EVENTOS: Se cargaron ${eventos.size} eventos")
                    _state.value = EventoState.Success(eventos)
                }.onFailure { error ->
                    println("DEBUG_EVENTOS: Error al cargar: ${error.message}")
                    _state.value = EventoState.Error(error.message ?: "Error desconocido")
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
