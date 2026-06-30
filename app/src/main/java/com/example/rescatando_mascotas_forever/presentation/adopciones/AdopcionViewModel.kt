package com.example.rescatando_mascotas_forever.presentation.adopciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdopcionViewModel : ViewModel() {

    private val _mascotas = MutableStateFlow<List<Mascota>>(emptyList())
    val mascotas: StateFlow<List<Mascota>> = _mascotas

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    private val _lastPage = MutableStateFlow(1)
    val lastPage: StateFlow<Int> = _lastPage

    private val _totalMascotas = MutableStateFlow(0)
    val totalMascotas: StateFlow<Int> = _totalMascotas

    init {
        cargarMascotas()
    }

    fun cargarMascotas(especie: String? = null, page: Int = 1) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _currentPage.value = page
            
            try {
                val response = RetrofitClient.mascotaApi.getMascotas(especie = if (especie == "Todas") null else especie, page = page)
                if (response.success && response.data != null) {
                    val gson = Gson()
                    val json = gson.toJson(response.data)
                    val element = gson.fromJson(json, JsonElement::class.java)

                    val list: List<Mascota> = when {
                        element == null || element.isJsonNull -> emptyList()
                        element.isJsonArray -> {
                            val listType = object : TypeToken<List<Mascota>>() {}.type
                            gson.fromJson(element, listType)
                        }
                        element.isJsonObject -> {
                            val obj = element.asJsonObject
                            
                            if (obj.has("current_page") && !obj.get("current_page").isJsonNull) {
                                _currentPage.value = obj.get("current_page").asInt
                            }
                            if (obj.has("last_page") && !obj.get("last_page").isJsonNull) {
                                _lastPage.value = obj.get("last_page").asInt
                            }
                            if (obj.has("total") && !obj.get("total").isJsonNull) {
                                _totalMascotas.value = obj.get("total").asInt
                            }

                            if (obj.has("data")) {
                                val dataField = obj.get("data")
                                if (dataField.isJsonArray) {
                                    val listType = object : TypeToken<List<Mascota>>() {}.type
                                    gson.fromJson(dataField, listType)
                                } else if (dataField.isJsonObject) {
                                    listOf(gson.fromJson(dataField, Mascota::class.java))
                                } else emptyList()
                            } else if (obj.has("id")) {
                                listOf(gson.fromJson(obj, Mascota::class.java))
                            } else emptyList()
                        }
                        else -> emptyList()
                    }
                    _mascotas.value = list
                } else {
                    _mascotas.value = emptyList()
                    if (!response.success) {
                        _error.value = response.message ?: "Error al obtener mascotas"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun nextPage(especie: String? = null) {
        if (_currentPage.value < _lastPage.value) {
            cargarMascotas(especie, _currentPage.value + 1)
        }
    }

    fun prevPage(especie: String? = null) {
        if (_currentPage.value > 1) {
            cargarMascotas(especie, _currentPage.value - 1)
        }
    }
}
