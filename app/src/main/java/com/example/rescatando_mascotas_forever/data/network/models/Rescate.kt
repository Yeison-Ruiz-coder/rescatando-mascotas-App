package com.example.rescatando_mascotas_forever.data.network.models

data class Rescate(
    val id: Int,
    val descripcion: String,
    val ubicacion: String,
    val estado: String
)