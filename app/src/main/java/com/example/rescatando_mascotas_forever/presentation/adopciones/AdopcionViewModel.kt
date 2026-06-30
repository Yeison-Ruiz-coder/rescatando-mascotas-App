package com.example.rescatando_mascotas_forever.presentation.adopciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.data.network.models.MascotaDataWrapper
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

    init {
        cargarMascotas()
    }

    fun cargarMascotas(especie: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val response = RetrofitClient.mascotaApi.getMascotas(especie = especie)
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
                            if (obj.has("data")) {
                                val dataField = obj.get("data")
                                if (dataField.isJsonArray) {
                                    val listType = object : TypeToken<List<Mascota>>() {}.type
                                    gson.fromJson(dataField, listType)
                                } else {
                                    listOf(gson.fromJson(dataField, Mascota::class.java))
                                }
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
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
