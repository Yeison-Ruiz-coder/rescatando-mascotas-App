package com.example.rescatando_mascotas_forever.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.local.SessionManager
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

    fun register(name: String, email: String, password: String, confirmPass: String, sessionManager: SessionManager) {
        val trimmedEmail = email.trim()
        val trimmedName = name.trim()

        if (trimmedName.isBlank() || trimmedEmail.isBlank() || password.isBlank() || confirmPass.isBlank()) {
            _state.value = RegisterState.Error("Por favor completa todos los campos")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            _state.value = RegisterState.Error("Correo electrónico no válido")
            return
        }

        if (password.length < 6) {
            _state.value = RegisterState.Error("La contraseña debe tener al menos 6 caracteres")
            return
        }

        if (password != confirmPass) {
            _state.value = RegisterState.Error("Las contraseñas no coinciden")
            return
        }

        viewModelScope.launch {
            _state.value = RegisterState.Loading
            try {
                val response = repository.register(trimmedName, trimmedEmail, password, confirmPass)
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
