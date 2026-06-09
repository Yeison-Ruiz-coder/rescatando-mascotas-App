package com.example.rescatando_mascotas_forever.data.network.models

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    @SerializedName(value = "nombre", alternate = ["name"])
    val nombre: String?,
    val apellidos: String?,
    val email: String?,
    val tipo: String?, // 'admin', 'user', 'fundacion', 'veterinaria'
    val estado: String?,
    val telefono: String?,
    val avatar: String?,
    @SerializedName("numero_documento")
    val numeroDocumento: String?
)

data class AuthResponse(
    val success: Boolean,
    val message: String?,
    val data: AuthData?
)

data class AuthData(
    @SerializedName(value = "token", alternate = ["access_token", "accessToken"])
    val token: String?,
    @SerializedName(value = "user", alternate = ["usuario"])
    val user: User?
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val nombre: String,
    val email: String,
    val password: String,
    @SerializedName("password_confirmation")
    val passwordConfirmation: String,
    val tipo: String = "user"
)
