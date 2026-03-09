package com.example.rescatando_mascotas_forever.data.network.models

data class Mascota(
    val id: Int,
    val nombre: String,
    val tipo: String,
    val raza: String,
    val edad: Int,
    val descripcion: String,
    val imagenUrl: String
)