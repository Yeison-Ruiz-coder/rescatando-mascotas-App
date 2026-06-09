package com.example.rescatando_mascotas_forever.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.api.RetrofitClient
import com.example.rescatando_mascotas_forever.data.network.models.Evento
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.data.repository.EventoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val eventoRepository: EventoRepository = EventoRepository(RetrofitClient.eventoApi)
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

    private val todasLasMascotas = listOf(
        Mascota(
            id = 1,
            nombre = "Luna",
            especie = "Perro",
            edadAprox = 2.0,
            genero = "Hembra",
            estado = "En adopcion",
            ubicacion = "Popayán, Cauca",
            descripcion = "Es muy juguetona y cariñosa.",
            fotoPrincipal = "https://images.dog.ceo/breeds/retriever-golden/n02099601_3004.jpg",
            aptoConNinos = true,
            aptoConOtrosAnimales = true,
            fundacionId = 1
        ),
        Mascota(
            id = 2,
            nombre = "Simba",
            especie = "Gato",
            edadAprox = 1.0,
            genero = "Macho",
            estado = "En adopcion",
            ubicacion = "Popayán, Cauca",
            descripcion = "Gatito rescatado, muy tranquilo.",
            fotoPrincipal = "https://cdn2.thecatapi.com/images/MTY3ODIyNQ.jpg",
            aptoConNinos = true,
            aptoConOtrosAnimales = false,
            fundacionId = 1
        ),
        Mascota(
            id = 5,
            nombre = "Bugs",
            especie = "Conejo",
            edadAprox = 0.5,
            genero = "Macho",
            estado = "En adopcion",
            ubicacion = "Popayán, Cauca",
            descripcion = "Pequeño conejo blanco.",
            fotoPrincipal = "https://images.pexels.com/photos/4001296/pexels-photo-4001296.jpeg",
            aptoConNinos = true,
            aptoConOtrosAnimales = true,
            fundacionId = 1
        ),
        Mascota(
            id = 6,
            nombre = "Piolín",
            especie = "Ave",
            edadAprox = 1.0,
            genero = "Macho",
            estado = "En adopcion",
            ubicacion = "Popayán, Cauca",
            descripcion = "Canario cantarín.",
            fotoPrincipal = "https://images.pexels.com/photos/2662434/pexels-photo-2662434.jpeg",
            aptoConNinos = true,
            aptoConOtrosAnimales = true,
            fundacionId = 1
        ),
        Mascota(
            id = 4,
            nombre = "Mora",
            especie = "Perro",
            edadAprox = 3.0,
            genero = "Hembra",
            estado = "En adopcion",
            ubicacion = "Popayán, Cauca",
            descripcion = "Busca una familia activa.",
            fotoPrincipal = "https://images.dog.ceo/breeds/labrador/n02099712_3503.jpg",
            aptoConNinos = false,
            aptoConOtrosAnimales = true,
            fundacionId = 1
        )
    )

    init {
        cargarDatosHome()
    }

    fun selectCategoria(categoria: String) {
        _selectedCategoria.value = categoria
        filtrarMascotas(categoria)
    }

    private fun filtrarMascotas(categoria: String) {
        val especieBuscada = when (categoria) {
            "Perros" -> "Perro"
            "Gatos" -> "Gato"
            "Conejos" -> "Conejo"
            "Aves" -> "Ave"
            else -> null // "Todos" o cualquier otro
        }
        
        _mascotas.value = if (especieBuscada != null) {
            todasLasMascotas.filter { it.especie == especieBuscada }
        } else {
            todasLasMascotas
        }
    }

    fun cargarDatosHome() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            // Inicializar con la categoría por defecto
            filtrarMascotas(_selectedCategoria.value)
            
            // Cargamos eventos reales
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
