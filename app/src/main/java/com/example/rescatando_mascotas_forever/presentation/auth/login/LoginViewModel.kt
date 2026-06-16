package com.example.rescatando_mascotas_forever.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.local.SessionManager
import com.example.rescatando_mascotas_forever.data.network.models.User
import com.example.rescatando_mascotas_forever.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun login(email: String, password: String, sessionManager: SessionManager) {
        val cleanEmail = email.trim()
        val cleanPassword = password // No trim en contraseña

        if (cleanEmail.isBlank() || cleanPassword.isBlank()) {
            _state.value = LoginState.Error("Por favor completa todos los campos")
            return
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            _state.value = LoginState.Error("Correo electrónico no válido")
            return
        }

        viewModelScope.launch {
            _state.value = LoginState.Loading
            try {
                val response = repository.login(cleanEmail, cleanPassword)
                val user = response.data?.user ?: response.user
                val token = response.data?.token ?: response.token
                
                if (user != null && token != null) {
                    sessionManager.saveSession(token, user)
                    _state.value = LoginState.Success(user)
                } else {
                    _state.value = LoginState.Error("Error: El servidor no devolvió el token o el usuario correctamente.")
                }
            } catch (e: Exception) {
                _state.value = LoginState.Error(e.message ?: "Error al iniciar sesión. Verifica tus credenciales.")
            }
        }
    }

    fun resetState() {
        _state.value = LoginState.Idle
    }
}
