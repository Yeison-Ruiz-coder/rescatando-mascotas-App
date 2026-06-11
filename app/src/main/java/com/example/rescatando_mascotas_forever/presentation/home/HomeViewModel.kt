package com.example.rescatando_mascotas_forever.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import com.example.rescatando_mascotas_forever.data.network.models.Evento
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.data.repository.EventoRepository
import com.example.rescatando_mascotas_forever.data.repository.MascotaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val eventoRepository: EventoRepository = EventoRepository(RetrofitClient.eventoApi),
    private val mascotaRepository: MascotaRepository = MascotaRepository()
) : ViewModel() {

    private val _mascotas = MutableStateFlow<List<Mascota>>(emptyList())
    val mascotas: StateFlow<List<Mascota>> = _mascotas

    private val _eventos = MutableStateFlow<List<Evento>>(emptyList())
    val eventos: StateFlow<List<Evento>> = _eventos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedCategoria = MutableStateFlow("Todos")
    val selectedCategoria: StateFlow<String> = _selectedCategoria

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private var todasLasMascotasList: List<Mascota> = emptyList()

    init {
        cargarDatosHome()
    }

    fun selectCategoria(categoria: String) {
        _selectedCategoria.value = categoria
        filtrarMascotasLocalmente(categoria, _searchQuery.value)
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        filtrarMascotasLocalmente(_selectedCategoria.value, query)
    }

    private fun filtrarMascotasLocalmente(categoria: String, query: String) {
        val especieBuscada = when (categoria) {
            "Perros" -> "Perro"
            "Gatos" -> "Gato"
            "Conejos" -> "Conejo"
            "Aves" -> "Ave"
            else -> null
        }
        
        var filtradas = todasLasMascotasList
        
        if (especieBuscada != null) {
            filtradas = filtradas.filter { it.especie.equals(especieBuscada, ignoreCase = true) }
        }
        
        if (query.isNotBlank()) {
            filtradas = filtradas.filter { 
                it.nombre.contains(query, ignoreCase = true) || 
                it.especie.contains(query, ignoreCase = true) ||
                (it.ubicacion?.contains(query, ignoreCase = true) ?: false)
            }
        }
        
        _mascotas.value = filtradas
    }

    fun cargarDatosHome() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            // 1. Cargamos mascotas desde Railway
            mascotaRepository.getMascotas().collect { result ->
                result.onSuccess { response ->
                    todasLasMascotasList = response.data.data
                    filtrarMascotasLocalmente(_selectedCategoria.value, _searchQuery.value)
                }.onFailure { e ->
                    _error.value = "Error al conectar con la base de datos: ${e.message}"
                }
            }
            
            // 2. Cargamos eventos reales
            eventoRepository.getEventos().collect { result ->
                result.onSuccess {
                    _eventos.value = it.take(3) 
                }.onFailure {
                }
            }
            
            _isLoading.value = false
        }
    }
}
