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
    val totalPages = 4

    // Paso 1: Datos Personales
    var nombre by mutableStateOf("")
    var apellido by mutableStateOf("")
    var documentoIdentidad by mutableStateOf("")
    var email by mutableStateOf("")
    var telefono by mutableStateOf("")
    var fechaNacimiento by mutableStateOf("")
    var ocupacion by mutableStateOf("")

    // Paso 2: Información de Vivienda
    var direccion by mutableStateOf("")
    var ciudad by mutableStateOf("")
    var departamento by mutableStateOf("")
    var codigoPostal by mutableStateOf("")
    var estadoCivil by mutableStateOf("")
    var cantidadHijos by mutableStateOf("0")
    var tipoVivienda by mutableStateOf("")
    var esPropietario by mutableStateOf("")

    // Paso 3: Compromisos
    var experienciaMascotas by mutableStateOf("")
    var motivoAdopcion by mutableStateOf("")
    var compromisoCuidado by mutableStateOf(false)
    var compromisoEsterilizacion by mutableStateOf(false)
    var compromisoSeguimiento by mutableStateOf(false)

    var isSaving by mutableStateOf(false)
    var saveSuccess by mutableStateOf(false)

    fun isStepValid(step: Int): Boolean {
        return when (step) {
            1 -> nombre.isNotBlank() && apellido.isNotBlank() && documentoIdentidad.isNotBlank() && 
                 email.isNotBlank() && telefono.isNotBlank()
            2 -> direccion.isNotBlank() && ciudad.isNotBlank()
            3 -> experienciaMascotas.isNotBlank() && motivoAdopcion.isNotBlank() && 
                 compromisoCuidado && compromisoEsterilizacion && compromisoSeguimiento
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
            // Aquí se conectará con el repositorio en el futuro
            delay(2000)
            isSaving = false
            saveSuccess = true
            onSuccess()
        }
    }
}
