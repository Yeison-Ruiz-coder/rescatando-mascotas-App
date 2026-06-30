package com.example.rescatando_mascotas_forever.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import com.example.rescatando_mascotas_forever.data.network.models.Evento
import com.example.rescatando_mascotas_forever.data.network.models.EventoPagination
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.data.network.models.MascotaDataWrapper
import com.example.rescatando_mascotas_forever.data.repository.EventoRepository
import com.example.rescatando_mascotas_forever.data.repository.MascotaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken

data class Foundation(
    val id: Int,
    val name: String,
    val city: String,
    val address: String,
    val isVerified: Boolean = true
)

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

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedCategoria = MutableStateFlow("Todos")
    val selectedCategoria: StateFlow<String> = _selectedCategoria

    private var todasLasMascotasList: List<Mascota> = emptyList()

    private val _selectedCity = MutableStateFlow("Todas las ciudades")
    val selectedCity: StateFlow<String> = _selectedCity

    private val _allFoundations = MutableStateFlow(listOf(
        Foundation(1, "Amigos Peludos", "Bucaramanga", "Carrera 25 # 10-05"),
        Foundation(2, "Corazón Animal", "Bogotá", "Calle 15 # 45-67"),
        Foundation(3, "El Refugio", "Barranquilla", "Calle 50 # 20-30"),
        Foundation(4, "Patitas Alegres", "Cali", "Calle 8 # 34-56"),
        Foundation(5, "Huellitas de Amor", "Popayán", "Centro Histórico"),
        Foundation(6, "Rescate Fiel", "Medellín", "El Poblado")
    ))

    val cities: StateFlow<List<String>> = _allFoundations
        .map { foundations -> listOf("Todas las ciudades") + foundations.map { it.city }.distinct().sorted() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf("Todas las ciudades"))

    val filteredFoundations: StateFlow<List<Foundation>> = combine(
        _searchQuery,
        _selectedCity,
        _allFoundations
    ) { query, city, foundations ->
        foundations.filter { foundation ->
            val matchesQuery = foundation.name.contains(query, ignoreCase = true) ||
                               foundation.city.contains(query, ignoreCase = true)
            val matchesCity = city == "Todas las ciudades" || foundation.city == city
            matchesQuery && matchesCity
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        cargarDatosHome()
    }

    fun selectCategoria(categoria: String) {
        _selectedCategoria.value = categoria
        filtrarMascotasLocalmente(categoria, _searchQuery.value)
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        filtrarMascotasLocalmente(_selectedCategoria.value, newQuery)
    }

    fun onCitySelected(city: String) {
        _selectedCity.value = city
    }

    fun clearFilters() {
        _searchQuery.value = ""
        _selectedCity.value = "Todas las ciudades"
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
            filtradas = filtradas.filter { it.especie?.equals(especieBuscada, ignoreCase = true) == true }
        }
        
        if (query.isNotBlank()) {
            filtradas = filtradas.filter { 
                it.nombre.contains(query, ignoreCase = true) || 
                (it.especie?.contains(query, ignoreCase = true) ?: false) ||
                (it.ubicacion?.contains(query, ignoreCase = true) ?: false)
            }
        }
        
        _mascotas.value = filtradas
    }

    fun cargarDatosHome() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            mascotaRepository.getMascotas().collect { result ->
                result.onSuccess { response ->
                    try {
                        val gson = Gson()
                        val json = gson.toJson(response.data)
                        val element = gson.fromJson(json, JsonElement::class.java)

                        val mascotas = when {
                            element.isJsonArray -> {
                                val listType = object : TypeToken<List<Mascota>>() {}.type
                                gson.fromJson<List<Mascota>>(element, listType)
                            }
                            element.isJsonObject -> {
                                val obj = element.asJsonObject
                                when {
                                    obj.has("data") && obj.get("data").isJsonArray -> {
                                        val listType = object : TypeToken<List<Mascota>>() {}.type
                                        gson.fromJson<List<Mascota>>(obj.get("data"), listType)
                                    }
                                    obj.has("id") -> listOf(gson.fromJson(element, Mascota::class.java))
                                    else -> emptyList()
                                }
                            }
                            else -> emptyList()
                        }
                        
                        // En el Home solo mostramos una muestra (ej: 8), no todos los 100 para evitar listas infinitas
                        todasLasMascotasList = mascotas?.take(8) ?: emptyList()
                        filtrarMascotasLocalmente(_selectedCategoria.value, _searchQuery.value)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        todasLasMascotasList = emptyList()
                    }
                }.onFailure { e ->
                    _error.value = "Error al cargar mascotas: ${e.message}"
                }
                _isLoading.value = false
            }
            
            eventoRepository.getEventos().collect { result ->
                val pagination = result.getOrNull()
                if (pagination != null) {
                    // Tomamos solo los últimos 3 eventos subidos (ordenados por ID descendente)
                    _eventos.value = pagination.data.sortedByDescending { it.id }.take(3)
                }
            }
        }
    }
}
