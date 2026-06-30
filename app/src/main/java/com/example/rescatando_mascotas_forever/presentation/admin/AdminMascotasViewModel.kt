package com.example.rescatando_mascotas_forever.presentation.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import com.google.gson.Gson
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AdminMascotasState {
    object Loading : AdminMascotasState()
    data class Success(val mascotas: List<Mascota>) : AdminMascotasState()
    data class Error(val message: String) : AdminMascotasState()
}

class AdminMascotasViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<AdminMascotasState>(AdminMascotasState.Loading)
    val uiState: StateFlow<AdminMascotasState> = _uiState.asStateFlow()

    init {
        fetchMascotas()
    }

    fun fetchMascotas() {
        viewModelScope.launch {
            _uiState.value = AdminMascotasState.Loading
            try {
                val response = RetrofitClient.mascotaApi.getAdminMascotas()
                val gson = Gson()

                if (response.success && response.data != null) {
                    // Convertimos el "Any" a un JsonElement para inspeccionarlo con seguridad
                    val jsonString = gson.toJson(response.data)
                    val element = gson.fromJson(jsonString, JsonElement::class.java)

                    // 1. Extraemos los elementos JSON crudos (manejando Array, Paginación o Objeto único)
                    val rawElements: List<JsonElement> = when {
                        element.isJsonArray -> element.asJsonArray.toList()
                        element.isJsonObject -> {
                            val obj = element.asJsonObject
                            when {
                                obj.has("data") && obj.get("data").isJsonArray -> {
                                    obj.get("data").asJsonArray.toList()
                                }
                                obj.has("id") || obj.has("nombre_mascota") -> {
                                    listOf(element)
                                }
                                else -> emptyList()
                            }
                        }
                        else -> emptyList()
                    }

                    // 2. Parseo INDIVIDUAL defensivo
                    // Si un registro de la DB está corrupto, lo saltamos en lugar de romper toda la lista
                    val mascotas = rawElements.mapNotNull { jsonItem ->
                        try {
                            gson.fromJson(jsonItem, Mascota::class.java)
                        } catch (e: Exception) {
                            Log.e("AdminMascotasVM", "Error parseando registro individual: ${e.message}")
                            null
                        }
                    }

                    _uiState.value = AdminMascotasState.Success(mascotas)
                } else {
                    _uiState.value = AdminMascotasState.Success(emptyList())
                }
            } catch (e: Exception) {
                Log.e("AdminMascotasViewModel", "Error crítico al cargar: ${e.message}", e)
                _uiState.value = AdminMascotasState.Error("Error de datos: Formato no reconocido o error de conexión")
            }
        }
    }

    fun deleteMascota(id: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.mascotaApi.adminDeleteMascota(id)
                if (response.success) {
                    fetchMascotas()
                }
            } catch (e: Exception) {
                Log.e("AdminMascotasViewModel", "Error al eliminar: ${e.message}")
            }
        }
    }
}
