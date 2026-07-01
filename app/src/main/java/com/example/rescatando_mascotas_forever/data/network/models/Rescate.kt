package com.example.rescatando_mascotas_forever.data.network.models

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class Rescate(
    val id: Int? = null,
    @SerializedName("fecha_rescate") val fechaRescate: String,
    @SerializedName("lugar_rescate") val lugarRescate: String,
    @SerializedName("descripcion_rescate") val descripcionRescate: String,
    val estado: String,
    @SerializedName("tipo_emergencia") val tipoEmergencia: String,
    val prioridad: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    @SerializedName("nombre_reportante") val nombreReportante: String? = null,
    @SerializedName("email_reportante") val emailReportante: String? = null,
    @SerializedName("telefono_reportante") val telefonoReportante: String? = null,
    @SerializedName("mascota_id") val mascotaId: Int? = null,
    @SerializedName("usuario_reporto_id") val usuarioReportoId: Int? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("foto_principal") val fotoPrincipal: String? = null,
    @SerializedName("galeria_fotos") val galeriaFotos: JsonElement? = null,
    @SerializedName("usuario_reporto") val usuarioReporto: UsuarioReporto? = null,
    @SerializedName("mascota") val mascota: Mascota? = null
)

fun Rescate.getGaleriaAsList(): List<String> {
    val element = this.galeriaFotos
    if (element == null || element.isJsonNull) return emptyList()
    return try {
        if (element.isJsonArray) {
            element.asJsonArray.mapNotNull { if (it.isJsonPrimitive) it.asString else null }
        } else if (element.isJsonPrimitive) {
            val jsonString = element.asString
            if (jsonString.startsWith("[") && jsonString.endsWith("]")) {
                com.google.gson.JsonParser.parseString(jsonString).asJsonArray.mapNotNull { 
                    if (it.isJsonPrimitive) it.asString else null 
                }
            } else {
                listOf(jsonString)
            }
        } else {
            emptyList()
        }
    } catch (e: Exception) {
        emptyList()
    }
}

data class UsuarioReporto(
    val id: Int,
    val nombre: String,
    val email: String? = null
)
