package com.example.rescatando_mascotas_forever.presentation.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.domain.usecases.adopcion.SolicitarAdopcionUseCase
import kotlinx.coroutines.launch

class AdminFormularioAdopcionViewModel(
    private val solicitarAdopcionUseCase: SolicitarAdopcionUseCase
) : ViewModel() {

    var currentPage by mutableIntStateOf(1)
    val totalPages = 5

    // Step 1
    var nombre by mutableStateOf("")
    var dni by mutableStateOf("")
    var edad by mutableStateOf("")

    // Step 2
    var ocupacion by mutableStateOf("")
    var telefono by mutableStateOf("")

    // Step 3
    var tipoVivienda by mutableStateOf("Casa")
    var tienePatio by mutableStateOf(false)
    var tieneProtecciones by mutableStateOf(false)

    // Step 4
    var integrantes by mutableStateOf("")
    var hayNinos by mutableStateOf(false)
    var estanDeAcuerdo by mutableStateOf(true)

    // Step 5
    var tieneOtrasMascotas by mutableStateOf(false)
    var experienciaPrevia by mutableStateOf("")
    var tiempoDiario by mutableStateOf("")
    var presupuestoVeterinario by mutableStateOf(true)

    var isSaving by mutableStateOf(false)
    var saveSuccess by mutableStateOf(false)

    fun isStepValid(step: Int): Boolean {
        return when (step) {
            1 -> nombre.isNotBlank() && dni.isNotBlank() && edad.isNotBlank()
            2 -> ocupacion.isNotBlank() && telefono.isNotBlank()
            3 -> true
            4 -> integrantes.isNotBlank()
            5 -> tiempoDiario.isNotBlank()
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

    fun guardarSolicitud(onSuccess: () -> Unit) {
        if (!isStepValid(currentPage)) return

        viewModelScope.launch {
            isSaving = true
            // Aquí se llamaría al use case con todos los datos
            // val solicitud = SolicitudAdopcion(...)
            // solicitarAdopcionUseCase(solicitud)
            
            // Simulamos una carga
            kotlinx.coroutines.delay(1500)
            
            isSaving = false
            saveSuccess = true
            onSuccess()
        }
    }
}
