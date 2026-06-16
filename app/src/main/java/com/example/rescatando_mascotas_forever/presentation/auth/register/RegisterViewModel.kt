package com.example.rescatando_mascotas_forever.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.local.SessionManager
import com.example.rescatando_mascotas_forever.data.network.models.RegisterRequest
import com.example.rescatando_mascotas_forever.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    fun register(
        nombre: String,
        email: String,
        password: String,
        confirmPass: String,
        tipo: String,
        apellidos: String? = null,
        telefono: String? = null,
        tipoDocumento: String? = null,
        numeroDocumento: String? = null,
        fechaNacimiento: String? = null,
        direccion: String? = null,
        pais: String? = null,
        ciudad: String? = null,
        nombreEntidad: String? = null,
        descripcion: String? = null,
        horarioAtencion: String? = null,
        registroSanitario: String? = null,
        capacidad: Int? = null,
        servicios: List<String>? = null,
        lat: Double? = null,
        lng: Double? = null,
        sessionManager: SessionManager
    ) {
        val trimmedEmail = email.trim()
        val trimmedNombre = nombre.trim()

        if (trimmedNombre.isBlank() || trimmedEmail.isBlank() || password.isBlank() || confirmPass.isBlank()) {
            _state.value = RegisterState.Error("Por favor completa los campos obligatorios")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            _state.value = RegisterState.Error("Correo electrónico no válido")
            return
        }

        if (password.length < 8) {
            _state.value = RegisterState.Error("La contraseña debe tener al menos 8 caracteres")
            return
        }

        if (password != confirmPass) {
            _state.value = RegisterState.Error("Las contraseñas no coinciden")
            return
        }

        viewModelScope.launch {
            _state.value = RegisterState.Loading
            try {
                val request = RegisterRequest(
                    nombre = trimmedNombre,
                    email = trimmedEmail,
                    password = password,
                    passwordConfirmation = confirmPass,
                    tipo = tipo,
                    apellidos = apellidos,
                    telefono = telefono,
                    tipoDocumento = tipoDocumento,
                    numeroDocumento = numeroDocumento,
                    fechaNacimiento = fechaNacimiento,
                    direccion = direccion,
                    pais = pais,
                    ciudad = ciudad,
                    nombreEntidad = nombreEntidad,
                    descripcion = descripcion,
                    horarioAtencion = horarioAtencion,
                    registroSanitario = registroSanitario,
                    capacidad = capacidad,
                    servicios = servicios,
                    lat = lat,
                    lng = lng
                )

                val response = repository.register(request)
                
                if (response.success || response.token != null) {
                    val user = response.data?.user ?: response.user
                    val token = response.data?.token ?: response.token
                    
                    if (user != null && token != null) {
                        sessionManager.saveSession(token, user)
                    }

                    _state.value = RegisterState.Success
                } else {
                    _state.value = RegisterState.Error(response.message ?: "Error en el registro")
                }
            } catch (e: Exception) {
                _state.value = RegisterState.Error(e.message ?: "Error al registrarse. Intenta con otro correo.")
            }
        }
    }

    fun resetState() {
        _state.value = RegisterState.Idle
    }
}
