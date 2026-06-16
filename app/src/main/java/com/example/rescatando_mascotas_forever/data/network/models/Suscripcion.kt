package com.example.rescatando_mascotas_forever.data.network.models

import com.google.gson.annotations.SerializedName

data class Suscripcion(
    val id: Int? = null,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("mascota_id") val mascotaId: Int,
    @SerializedName("monto_mensual") val montoMensual: Double,
    val frecuencia: String, // unica, mensual, trimestral, anual
    @SerializedName("fecha_inicio") val fechaInicio: String,
    @SerializedName("fecha_fin") val fechaFin: String? = null,
    @SerializedName("mensaje_apoyo") val mensajeApoyo: String? = null,
    val estado: String, // activo, pausado, cancelado, finalizado
    val mascota: Mascota? = null,
    val user: User? = null
)

data class SuscripcionResponse(
    val success: Boolean,
    val data: List<Suscripcion>,
    val message: String
)

data class SingleSuscripcionResponse(
    val success: Boolean,
    val data: Suscripcion,
    val message: String
)
