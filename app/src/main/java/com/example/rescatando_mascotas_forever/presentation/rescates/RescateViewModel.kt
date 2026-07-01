package com.example.rescatando_mascotas_forever.presentation.rescates

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import com.example.rescatando_mascotas_forever.data.repository.RescateRepository
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class RescateReportState {
    object Idle : RescateReportState()
    object Loading : RescateReportState()
    object Success : RescateReportState()
    data class Error(val message: String) : RescateReportState()
}

class RescateViewModel(
    private val repository: RescateRepository = RescateRepository(RetrofitClient.rescateApi)
) : ViewModel() {

    private val _reportState = MutableStateFlow<RescateReportState>(RescateReportState.Idle)
    val reportState = _reportState.asStateFlow()

    private val _currentLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val currentLocation = _currentLocation.asStateFlow()

    private val _galeriaUris = MutableStateFlow<List<Uri>>(emptyList())
    val galeriaUris = _galeriaUris.asStateFlow()

    @SuppressLint("MissingPermission")
    fun obtenerUbicacionActual(context: Context) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        viewModelScope.launch {
            try {
                // Captura la ubicación actual con alta precisión
                val location = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    null
                ).await()
                
                location?.let {
                    _currentLocation.value = Pair(it.latitude, it.longitude)
                }
            } catch (e: Exception) {
                _reportState.value = RescateReportState.Error("No se pudo obtener la ubicación: ${e.message}")
            }
        }
    }

    fun setGaleriaUris(uris: List<Uri>) {
        _galeriaUris.value = uris
    }

    fun addGaleriaUri(uri: Uri) {
        _galeriaUris.value = _galeriaUris.value + uri
    }

    fun removeGaleriaUri(uri: Uri) {
        _galeriaUris.value = _galeriaUris.value.filter { it != uri }
    }

    fun reportarRescate(
        context: Context,
        lugar: String,
        descripcion: String,
        fecha: String,
        tipo: String,
        prioridad: String,
        paraFundaciones: Boolean,
        paraVeterinarias: Boolean,
        paraAdmin: Boolean,
        nombre: String?,
        email: String?,
        telefono: String?,
        lat: Double?,
        lng: Double?,
        imageUri: Uri?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _reportState.value = RescateReportState.Loading
            repository.reportarRescate(
                context = context,
                lugar = lugar,
                descripcion = descripcion,
                fecha = fecha,
                tipo = tipo,
                prioridad = prioridad,
                paraFundaciones = paraFundaciones,
                paraVeterinarias = paraVeterinarias,
                paraAdmin = paraAdmin,
                nombre = nombre,
                email = email,
                telefono = telefono,
                lat = lat,
                lng = lng,
                imageUri = imageUri,
                galeriaUris = _galeriaUris.value
            ).onSuccess {
                _reportState.value = RescateReportState.Success
                onSuccess()
            }.onFailure { error ->
                _reportState.value = RescateReportState.Error(error.message ?: "Error desconocido")
            }
        }
    }

    fun resetState() {
        _reportState.value = RescateReportState.Idle
        _currentLocation.value = null
        _galeriaUris.value = emptyList()
    }
}
