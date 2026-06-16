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
    val success: Boolean = true,
    val message: String? = null,
    val data: AuthData? = null,
    @SerializedName(value = "token", alternate = ["access_token", "accessToken"])
    val token: String? = null,
    @SerializedName(value = "user", alternate = ["usuario"])
    val user: User? = null
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
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("apellidos")
    val apellidos: String = "",
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("password_confirmation")
    val passwordConfirmation: String,
    @SerializedName("tipo")
    val tipo: String = "user"
)
