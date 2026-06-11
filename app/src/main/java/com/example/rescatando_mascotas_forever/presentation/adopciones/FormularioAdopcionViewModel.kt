package com.example.rescatando_mascotas_forever.presentation.adopciones

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FormularioAdopcionViewModel : ViewModel() {

    var currentPage by mutableIntStateOf(1)
    val totalPages = 3

    // Step 1: Información Personal
    var nombreCompleto by mutableStateOf("")
    var edad by mutableStateOf("")
    var direccion by mutableStateOf("")
    var telefono by mutableStateOf("")

    // Step 2: Entorno y Motivo
    var tieneOtrasMascotas by mutableStateOf(false)
    var motivo by mutableStateOf("")
    var tiempoDisponible by mutableStateOf("")

    // Step 3: Compromiso
    var aceptaTerminos by mutableStateOf(false)

    var isSaving by mutableStateOf(false)
    var saveSuccess by mutableStateOf(false)

    fun isStepValid(step: Int): Boolean {
        return when (step) {
            1 -> nombreCompleto.isNotBlank() && edad.isNotBlank() && direccion.isNotBlank() && telefono.isNotBlank()
            2 -> motivo.isNotBlank() && tiempoDisponible.isNotBlank()
            3 -> aceptaTerminos
            else -> true
        }
    }

    fun nextStep() {
        if (currentPage < totalPages && isStepValid(currentPage)) {
            currentPage++
        }
    }

    fun previousStep() {
        if (currentPage > 1) {
            currentPage--
        }
    }

    fun enviarSolicitud(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isSaving = true
            // Simulación de envío
            delay(2000)
            isSaving = false
            saveSuccess = true
            onSuccess()
        }
    }
}
