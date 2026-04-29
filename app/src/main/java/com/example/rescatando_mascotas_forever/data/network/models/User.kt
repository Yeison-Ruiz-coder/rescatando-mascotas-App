// Archivo: C:/Users/User/AndroidStudioProjects/rescatandomascotasforever/app/src/main/java/com/example/rescatando_mascotas_forever/data/network/models/User.kt

package com.example.rescatando_mascotas_forever.data.network.models

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val nombre: String,
    val apellidos: String?,
    val email: String,
    val tipo: String, // 'admin', 'user', 'fundacion', 'veterinaria'
    val estado: String?,
    val telefono: String?,
    val avatar: String?,
    @SerializedName("numero_documento")
    val numeroDocumento: String?
)

data class AuthResponse(
    val token: String,
    val user: User
)

data class LoginRequest(
    val email: String,
    val password: String
)