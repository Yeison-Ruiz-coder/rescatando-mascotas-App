package com.example.rescatando_mascotas_forever.presentation.fundacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.data.network.models.MascotaDataWrapper
import com.example.rescatando_mascotas_forever.data.network.models.MascotaResponse
import com.example.rescatando_mascotas_forever.data.repository.MascotaRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class FoundationMascotasState {
    object Idle : FoundationMascotasState()
    object Loading : FoundationMascotasState()
    data class Success(val mascotas: List<Mascota>) : FoundationMascotasState()
    data class Error(val message: String) : FoundationMascotasState()
}

class FoundationMascotasViewModel(
    private val repository: MascotaRepository = MascotaRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<FoundationMascotasState>(FoundationMascotasState.Idle)
    val state: StateFlow<FoundationMascotasState> = _state.asStateFlow()

    init {
        loadMascotas()
    }

    fun loadMascotas() {
        viewModelScope.launch {
            _state.value = FoundationMascotasState.Loading
            repository.getMisMascotas().collect { result ->
                result.onSuccess { response ->
                    val mascotas = try {
                        val gson = Gson()
                        val json = gson.toJson(response.data)
                        
                        // Intentamos parsear como MascotaDataWrapper (paginado)
                        val wrapper = try {
                            gson.fromJson(json, MascotaDataWrapper::class.java)
                        } catch (e: Exception) {
                            null
                        }

                        if (wrapper?.data != null) {
                            wrapper.data
                        } else {
                            // Si falla, intentamos parsear como una lista directa de mascotas
                            val listType = object : TypeToken<List<Mascota>>() {}.type
                            gson.fromJson<List<Mascota>>(json, listType) ?: emptyList()
                        }
                    } catch (e: Exception) {
                        emptyList()
                    }
                    _state.value = FoundationMascotasState.Success(mascotas)
                }.onFailure { error ->
                    _state.value = FoundationMascotasState.Error(error.message ?: "Error desconocido")
                }
            }
        }
    }

    fun deleteMascota(id: Int) {
        viewModelScope.launch {
            repository.deleteMascota(id).onSuccess {
                loadMascotas()
            }.onFailure {
                // Manejar error de eliminación
            }
        }
    }
}
