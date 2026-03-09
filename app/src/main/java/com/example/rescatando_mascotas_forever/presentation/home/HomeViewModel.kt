package com.example.rescatando_mascotas_forever.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _mascotas = MutableStateFlow<List<Mascota>>(emptyList())
    val mascotas: StateFlow<List<Mascota>> = _mascotas

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        cargarMascotas()
    }

    fun cargarMascotas(especie: String? = null, estado: String? = "En adopcion") {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            // Simulamos una pequeña espera de red para que se vea el cargando
            delay(1000)

            // Datos de prueba (MOCK DATA)
            val listaPrueba = listOf(
                Mascota(
                    id = 1,
                    nombre = "Luna",
                    especie = "Perro",
                    edadAprox = 2,
                    genero = "Hembra",
                    estado = "En adopcion",
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
                    edadAprox = 1,
                    genero = "Macho",
                    estado = "En adopcion",
                    descripcion = "Gatito rescatado, muy tranquilo.",
                    fotoPrincipal = "https://images.ctfassets.net/denf86kkcx7r/4IPlg4Qazd4sFRuCUHIJ1T/f6c71da7eec727babcd554d843a528b8/gatocomuneuropeo-97",
                    aptoConNinos = true,
                    aptoConOtrosAnimales = false,
                    fundacionId = 1
                ),
                Mascota(
                    id = 3,
                    nombre = "Rocky",
                    especie = "Perro",
                    edadAprox = 4,
                    genero = "Macho",
                    estado = "Adoptado",
                    descripcion = "Ya encontró un hogar feliz.",
                    fotoPrincipal = "https://images.ctfassets.net/denf86kkcx7r/HJO06XFEAWjMW42CkMPQz/c3cb44ef5b0815101349affd2353033e/Beagle.webp?fm=webp&w=913",
                    aptoConNinos = true,
                    aptoConOtrosAnimales = true,
                    fundacionId = 1
                ),
                Mascota(
                    id = 4,
                    nombre = "Mora",
                    especie = "Perro",
                    edadAprox = 3,
                    genero = "Hembra",
                    estado = "En adopcion",
                    descripcion = "Busca una familia activa.",
                    fotoPrincipal = "https://images.dog.ceo/breeds/labrador/n02099712_3503.jpg",
                    aptoConNinos = false,
                    aptoConOtrosAnimales = true,
                    fundacionId = 1
                )
            )

            _mascotas.value = listaPrueba
            _isLoading.value = false
        }
    }
}
