package com.example.rescatando_mascotas_forever.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class Foundation(
    val id: Int,
    val name: String,
    val city: String,
    val address: String,
    val isVerified: Boolean = true
)

class HomeViewModel : ViewModel() {

    private val _mascotas = MutableStateFlow<List<Mascota>>(emptyList())
    val mascotas: StateFlow<List<Mascota>> = _mascotas

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // --- ESTADOS DE FUNDACIONES ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

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

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onCitySelected(city: String) {
        _selectedCity.value = city
    }

    fun clearFilters() {
        _searchQuery.value = ""
        _selectedCity.value = "Todas las ciudades"
    }

    init {
        cargarMascotas()
    }

    fun cargarMascotas() {
        // Lógica de carga de mascotas si es necesaria
    }
}
