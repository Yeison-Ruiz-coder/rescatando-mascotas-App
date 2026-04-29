package com.example.rescatando_mascotas_forever.data.repository

import com.example.rescatando_mascotas_forever.data.network.api.RetrofitClient
import com.example.rescatando_mascotas_forever.data.network.models.AuthResponse
import com.example.rescatando_mascotas_forever.data.network.models.LoginRequest

class AuthRepository {
    private val authApi = RetrofitClient.authApi

    suspend fun login(email: String, password: String): AuthResponse {
        return authApi.login(LoginRequest(email, password))
    }
}
