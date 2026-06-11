package com.example.rescatando_mascotas_forever.data.network.models

import com.google.gson.annotations.SerializedName

data class Evento(
    val id: Int,
    @SerializedName("nombre_evento")
    val nombre: String,
    @SerializedName("lugar_evento")
    val lugar: String,
    val descripcion: String?,
    @SerializedName("fecha_evento")
    val fecha: String,
    @SerializedName("imagen_url")
    val imagenUrl: String?,
    @SerializedName("imagen_public_id")
    val imagenPublicId: String?,
    @SerializedName("fundacion_id")
    val fundacionId: Int?,
    val tipo: String?,
    val likes: Int?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("deleted_at")
    val deletedAt: String?
)

data class EventoResponse(
    val success: Boolean,
    val data: List<Evento>
)
