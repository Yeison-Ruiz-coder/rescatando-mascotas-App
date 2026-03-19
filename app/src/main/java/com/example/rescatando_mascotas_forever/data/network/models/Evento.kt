package com.example.rescatando_mascotas_forever.data.network.models

data class Evento(
    val id: Int,
    val titulo: String,
    val fecha: String,
    val hora: String,
    val precio: String, // "Gratis" o "$35.000"
    val ubicacion: String,
    val descripcion: String,
    val imagenUrl: String,
    val etiqueta: String, // "DESTACADO", "TALLER", "CONCURSO"
    val confirmados: Int? = null,
    val cuposActuales: Int? = null,
    val cuposTotales: Int? = null
)
