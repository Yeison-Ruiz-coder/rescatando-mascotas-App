package com.example.rescatando_mascotas_forever.presentation.eventos

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.models.Evento
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import com.example.rescatando_mascotas_forever.data.repository.EventoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class EventoState {
    object Loading : EventoState()
    data class Success(
        val eventos: List<Evento>,
        val hasMore: Boolean = false,
        val isNextPageLoading: Boolean = false
    ) : EventoState()
    data class Error(val message: String) : EventoState()
}

class EventoViewModel(
    private val repository: EventoRepository = EventoRepository(RetrofitClient.eventoApi)
) : ViewModel() {

    private val _state = MutableStateFlow<EventoState>(EventoState.Loading)
    val state: StateFlow<EventoState> = _state.asStateFlow()

    private var currentPage = 1
    private var lastPage = 1
    private var allEventosList = mutableListOf<Evento>()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Todos")
    val selectedCategory = _selectedCategory.asStateFlow()

    val filteredEventos: StateFlow<List<Evento>> = combine(
        _state,
        _searchText,
        _selectedCategory
    ) { state, query, category ->
        if (state is EventoState.Success) {
            state.eventos.filter { evento ->
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
        currentPage = 1
        allEventosList.clear()
        fetchPage(currentPage)
    }

    fun loadNextPage() {
        val currentState = _state.value
        if (currentState is EventoState.Success && !currentState.isNextPageLoading && currentPage < lastPage) {
            _state.value = currentState.copy(isNextPageLoading = true)
            fetchPage(currentPage + 1)
        }
    }

    private fun fetchPage(page: Int) {
        viewModelScope.launch {
            if (page == 1) _state.value = EventoState.Loading
            
            repository.getEventos(page).collect { result ->
                result.onSuccess { pagination ->
                    currentPage = pagination.currentPage
                    lastPage = pagination.lastPage
                    
                    if (page == 1) {
                        allEventosList = pagination.data.toMutableList()
                    } else {
                        allEventosList.addAll(pagination.data)
                    }

                    _state.value = EventoState.Success(
                        eventos = allEventosList.toList(),
                        hasMore = currentPage < lastPage,
                        isNextPageLoading = false
                    )
                }.onFailure { error ->
                    if (page == 1) {
                        _state.value = EventoState.Error(error.message ?: "Error desconocido")
                    } else {
                        val currentState = _state.value
                        if (currentState is EventoState.Success) {
                            _state.value = currentState.copy(isNextPageLoading = false)
                        }
                    }
                }
            }
        }
    }

    fun crearEvento(context: Context, evento: Evento, imageUri: Uri?, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.value = EventoState.Loading
            repository.crearEvento(context, evento, imageUri).onSuccess {
                getEventos()
                onSuccess()
            }.onFailure { error ->
                _state.value = EventoState.Error("Error al crear: ${error.message}")
            }
        }
    }

    fun actualizarEvento(context: Context, id: Int, evento: Evento, imageUri: Uri?, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.value = EventoState.Loading
            repository.actualizarEvento(context, id, evento, imageUri).onSuccess {
                getEventos()
                onSuccess()
            }.onFailure { error ->
                _state.value = EventoState.Error("Error al actualizar: ${error.message}")
            }
        }
    }

    fun eliminarEvento(id: Int) {
        viewModelScope.launch {
            repository.eliminarEvento(id).onSuccess {
                getEventos()
            }.onFailure { error ->
                _state.value = EventoState.Error(error.message ?: "Error al eliminar")
            }
        }
    }
}
