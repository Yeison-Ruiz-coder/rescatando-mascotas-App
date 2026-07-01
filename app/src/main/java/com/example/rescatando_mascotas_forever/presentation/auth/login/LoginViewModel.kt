package com.example.rescatando_mascotas_forever.presentation.auth.login

import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.local.SessionManager
import com.example.rescatando_mascotas_forever.data.network.models.User
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import com.example.rescatando_mascotas_forever.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.HttpException
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

    fun loginWithGoogle(context: Context, sessionManager: SessionManager) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            try {
                val credentialManager = CredentialManager.create(context)
                
                // IMPORTANTE: Este debe ser un ID de cliente de tipo "WEB" en Google Cloud Console
                val serverClientId = "486049342985-r2r8kslmctovk296k1pslki02imlthed.apps.googleusercontent.com"
                
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(serverClientId)
                    .setAutoSelectEnabled(false)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = credentialManager.getCredential(context, request)
                val credential = result.credential

                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    
                    val userEmail = googleIdTokenCredential.id
                    val displayName = googleIdTokenCredential.displayName ?: "Usuario Google"
                    val photoUrl = googleIdTokenCredential.profilePictureUri?.toString()
                    
                    val dummyUser = User(
                        id = 0,
                        nombre = displayName,
                        apellidos = "",
                        email = userEmail,
                        tipo = "user",
                        estado = "activo",
                        telefono = null,
                        avatar = photoUrl,
                        numeroDocumento = null
                    )
                    
                    sessionManager.saveSession("google_token_dummy", dummyUser)
                    _state.value = LoginState.Success(dummyUser)
                } else {
                    _state.value = LoginState.Error("Tipo de cuenta no soportado")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error en Google Sign-In: ${e.message}", e)
                val errorMessage = when (e) {
                    is androidx.credentials.exceptions.GetCredentialException -> {
                        if (e.message?.contains("cancel", ignoreCase = true) == true) {
                            "Inicio de sesión cancelado"
                        } else {
                            "No se pudo conectar con Google. Verifica tu conexión."
                        }
                    }
                    else -> "Error al iniciar sesión con Google"
                }
                _state.value = LoginState.Error(errorMessage)
            } finally {
                if (_state.value is LoginState.Loading) {
                    _state.value = LoginState.Idle
                }
            }
        }
    }

    fun login(email: String, password: String, sessionManager: SessionManager) {
        val cleanEmail = email.trim()
        val cleanPassword = password

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
                    RetrofitClient.setToken(token)
                    _state.value = LoginState.Success(user)
                } else {
                    _state.value = LoginState.Error("Error: El servidor no devolvió el token o el usuario correctamente.")
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val message = try {
                    val errorObj = com.google.gson.Gson().fromJson(errorBody, com.example.rescatando_mascotas_forever.data.network.models.AuthResponse::class.java)
                    errorObj.message ?: "Credenciales inválidas"
                } catch (ex: Exception) {
                    if (e.code() == 401) "Correo o contraseña incorrectos" 
                    else "Error del servidor (${e.code()})"
                }
                _state.value = LoginState.Error(message)
            } catch (e: Exception) {
                _state.value = LoginState.Error("Error de conexión. Verifica tu internet.")
            }
        }
    }

    fun resetState() {
        _state.value = LoginState.Idle
    }
}
