package com.example.rescatando_mascotas_forever.presentation.veterinarias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.models.Veterinaria
import com.example.rescatando_mascotas_forever.data.repository.VeterinariaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class VeterinariaState {
    object Loading : VeterinariaState()
    data class Success(val veterinarias: List<Veterinaria>) : VeterinariaState()
    data class Error(val message: String) : VeterinariaState()
}

class VeterinariaViewModel(
    private val repository: VeterinariaRepository = VeterinariaRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<VeterinariaState>(VeterinariaState.Loading)
    val uiState: StateFlow<VeterinariaState> = _uiState.asStateFlow()

    init {
        getVeterinarias()
    }

    fun getVeterinarias() {
        viewModelScope.launch {
            _uiState.value = VeterinariaState.Loading
            repository.getVeterinarias().collect { result ->
                result.onSuccess { response ->
                    _uiState.value = VeterinariaState.Success(response.data.data)
                }.onFailure { exception ->
                    _uiState.value = VeterinariaState.Error(exception.message ?: "Error desconocido")
                }
            }
        }
    }
}
