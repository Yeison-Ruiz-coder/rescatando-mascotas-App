package com.example.rescatando_mascotas_forever.data.repository

import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import com.example.rescatando_mascotas_forever.data.network.models.AuthResponse
import com.example.rescatando_mascotas_forever.data.network.models.LoginRequest
import com.example.rescatando_mascotas_forever.data.network.models.RegisterRequest

class AuthRepository {
    private val authApi = RetrofitClient.authApi

    suspend fun login(email: String, password: String): AuthResponse {
        return authApi.login(LoginRequest(email, password))
    }

    suspend fun register(nombre: String, email: String, password: String, passwordConfirmation: String): AuthResponse {
        return authApi.register(RegisterRequest(nombre = nombre, email = email, password = password, passwordConfirmation = passwordConfirmation))
    }
}
