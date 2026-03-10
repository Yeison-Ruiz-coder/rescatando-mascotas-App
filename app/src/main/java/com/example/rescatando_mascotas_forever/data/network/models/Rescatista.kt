package com.example.rescatando_mascotas_forever.data.network.models

data class Rescatista(
    val id: Int,
    val nombre: String,
    val fotoUrl: String,
    val disponibilidad: String,
    val municipio: String,
    val barrio: String,
    val especialidad: String,
    val organizacion: String,
    val estado: String, // "Disponible" o "Ocupado"
    val whatsapp: String,
    val telefono: String
)
