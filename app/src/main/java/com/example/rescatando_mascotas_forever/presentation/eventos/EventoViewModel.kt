package com.example.rescatando_mascotas_forever.presentation.eventos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.models.Evento
import com.example.rescatando_mascotas_forever.data.repository.EventoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class EventoState {
    object Loading : EventoState()
    data class Success(val eventos: List<Evento>, val isLastPage: Boolean = false) : EventoState()
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

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Todos")
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    
    private var isRemoteLastPage = false

    val filteredEventos: StateFlow<List<Evento>> = combine(
        _state,
        _searchText,
        _selectedCategory
    ) { state, query, category ->
        if (state is EventoState.Success) {
            state.eventos.filter { evento ->
                val matchesCat = when (category) {
                    "Todos", "All" -> true
                    "Gratis" -> evento.costo?.contains("Gratis", ignoreCase = true) == true
                    "Adopciones" -> evento.nombre.contains("Adopción", ignoreCase = true) || 
                                   evento.categoria?.contains("Adopción", ignoreCase = true) == true
                    else -> evento.tipo?.contains(category, ignoreCase = true) == true ||
                            evento.categoria?.contains(category, ignoreCase = true) == true
                }

                val matchesSearch = query.isEmpty() ||
                        evento.nombre.contains(query, ignoreCase = true) ||
                        evento.lugar.contains(query, ignoreCase = true) ||
                        evento.organizador?.contains(query, ignoreCase = true) == true
                
                matchesCat && matchesSearch
            }
        } else {
            emptyList()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        getEventos(1)
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }

    fun getEventos(page: Int = 1) {
        _currentPage.value = page
        _state.value = EventoState.Loading
        
        viewModelScope.launch {
            repository.getEventos(page).collect { result ->
                result.onSuccess { pagination ->
                    isRemoteLastPage = pagination.currentPage >= pagination.lastPage
                    _state.value = EventoState.Success(
                        eventos = pagination.data,
                        isLastPage = isRemoteLastPage
                    )
                }.onFailure { error ->
                    _state.value = EventoState.Error(error.message ?: "Error desconocido")
                }
            }
        }
    }

    fun nextPage() {
        if (!isRemoteLastPage) {
            getEventos(_currentPage.value + 1)
        }
    }

    fun prevPage() {
        if (_currentPage.value > 1) {
            getEventos(_currentPage.value - 1)
        }
    }

    fun getEventoById(id: Int) {
        val currentState = _state.value
        if (currentState is EventoState.Success) {
            _eventoDetalle.value = currentState.eventos.find { it.id == id }
        }

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
