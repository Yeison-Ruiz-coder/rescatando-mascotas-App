package com.example.rescatando_mascotas_forever.data.network.models

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class Suscripcion(
    val id: Int? = null,
    @SerializedName("user_id") val userId: JsonElement? = null,
    @SerializedName("mascota_id") val mascotaId: JsonElement? = null,
    @SerializedName("monto_mensual") val montoMensual: JsonElement? = null,
    val frecuencia: String? = null,
    @SerializedName("fecha_inicio") val fechaInicio: String? = null,
    @SerializedName("fecha_fin") val fechaFin: String? = null,
    @SerializedName("mensaje_apoyo") val mensajeApoyo: String? = null,
    val estado: String? = "pendiente",
    @SerializedName("es_demo") val esDemo: JsonElement? = null,
    @SerializedName("payment_method") val paymentMethod: String? = null,
    @SerializedName("payment_reference") val paymentReference: String? = null,
    val mascota: Mascota? = null,
    val user: User? = null
)

fun JsonElement?.toSafeDouble(): Double {
    if (this == null || this.isJsonNull) return 0.0
    return try {
        if (this.isJsonPrimitive) {
            if (this.asJsonPrimitive.isNumber) this.asDouble else this.asString.toDoubleOrNull() ?: 0.0
        } else 0.0
    } catch (e: Exception) {
        0.0
    }
}

data class SuscripcionResponse(
    val success: Boolean,
    val data: JsonElement?, // Cambiado a JsonElement para manejar paginación u objetos
    val message: String
)

data class SingleSuscripcionResponse(
    val success: Boolean,
    val data: Suscripcion,
    val message: String
)
