package com.example.rescatando_mascotas_forever.data.network.models

data class Adopcion(
    val id: Int,
    val mascotaId: Int,
    val usuarioId: Int,
    val estado: String
)