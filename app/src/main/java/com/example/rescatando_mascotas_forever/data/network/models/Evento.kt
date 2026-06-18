package com.example.rescatando_mascotas_forever.data.network.models

import com.google.gson.annotations.SerializedName

data class Evento(
    val id: Int = 0,
    @SerializedName(value = "nombre_evento", alternate = ["nombre", "titulo"])
    val nombre: String = "",
    @SerializedName(value = "lugar_evento", alternate = ["lugar", "ubicacion"])
    val lugar: String = "",
    val descripcion: String? = null,
    @SerializedName(value = "fecha_evento", alternate = ["fecha"])
    val fecha: String = "",
    @SerializedName(value = "imagen_url", alternate = ["foto_principal", "imagen"])
    val imagenUrl: String? = null,
    @SerializedName("imagen_public_id")
    val imagenPublicId: String? = null,
    @SerializedName(value = "fundacion_id", alternate = ["fundacion"])
    val fundacionId: Int? = null,
    val tipo: String? = null,
    val likes: Int? = 0,
    @SerializedName("total_asistentes")
    val totalAsistentes: Int? = 0,
    @SerializedName("usuario_confirmado")
    val usuarioConfirmado: Boolean? = false,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    @SerializedName("deleted_at")
    val deletedAt: String? = null
)

data class EventoResponse(
    val success: Boolean,
    val message: String?,
    val data: EventoPagination?
)

data class EventoPagination(
    @SerializedName("current_page")
    val currentPage: Int,
    val data: List<Evento>,
    @SerializedName("last_page")
    val lastPage: Int,
    val total: Int
)
