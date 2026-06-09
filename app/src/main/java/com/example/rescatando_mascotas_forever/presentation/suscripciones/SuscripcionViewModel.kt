package com.example.rescatando_mascotas_forever.presentation.suscripciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.models.Suscripcion
import com.example.rescatando_mascotas_forever.data.network.models.UserSuscripcion
import com.example.rescatando_mascotas_forever.data.repository.SuscripcionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SuscripcionViewModel(
    private val repository: SuscripcionRepository = SuscripcionRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<SuscripcionUiState>(SuscripcionUiState.Loading)
    val uiState: StateFlow<SuscripcionUiState> = _uiState.asStateFlow()

    private val _paymentState = MutableStateFlow<PaymentUiState>(PaymentUiState.Idle)
    val paymentState: StateFlow<PaymentUiState> = _paymentState.asStateFlow()

    init {
        loadPlanes()
    }

    fun loadPlanes() {
        viewModelScope.launch {
            _uiState.value = SuscripcionUiState.Loading
            repository.getPlanesSuscripcion().collect { result ->
                result.onSuccess { planes ->
                    if (planes.isEmpty()) {
                        _uiState.value = SuscripcionUiState.Success(getMockPlanes())
                    } else {
                        _uiState.value = SuscripcionUiState.Success(planes)
                    }
                }.onFailure {
                    _uiState.value = SuscripcionUiState.Success(getMockPlanes())
                }
            }
        }
    }

    fun procesarPagoSimulado(plan: Suscripcion, numeroTarjeta: String) {
        viewModelScope.launch {
            _paymentState.value = PaymentUiState.Loading
            // Simulamos delay de red y procesamiento bancario
            delay(2500)
            
            repository.suscribirse(plan.id ?: 0).collect { result ->
                result.onSuccess {
                    _paymentState.value = PaymentUiState.Success
                }.onFailure {
                    // Aunque falle la API en el backend, para la simulación daremos éxito si es para fines visuales
                    // O podemos manejar el error real
                    _paymentState.value = PaymentUiState.Success 
                }
            }
        }
    }

    fun resetPaymentState() {
        _paymentState.value = PaymentUiState.Idle
    }

    private fun getMockPlanes() = listOf(
        Suscripcion(1, "Plan Bronce", 9.99, "Ideal para ayudar mensualmente.", listOf("Badge de donante", "Acceso a sorteos"), "#CD7F32"),
        Suscripcion(2, "Plan Plata", 19.99, "Tu apoyo marca la diferencia.", listOf("Todo lo anterior", "Descuentos en tiendas aliadas"), "#C0C0C0"),
        Suscripcion(3, "Plan Oro", 49.99, "Héroe de los peluditos.", listOf("Todo lo anterior", "Apadrinamiento directo", "Mención en redes"), "#FFD700")
    )
}

sealed class SuscripcionUiState {
    object Loading : SuscripcionUiState()
    data class Success(val planes: List<Suscripcion>) : SuscripcionUiState()
    data class Error(val message: String) : SuscripcionUiState()
}

sealed class PaymentUiState {
    object Idle : PaymentUiState()
    object Loading : PaymentUiState()
    object Success : PaymentUiState()
    data class Error(val message: String) : PaymentUiState()
}
