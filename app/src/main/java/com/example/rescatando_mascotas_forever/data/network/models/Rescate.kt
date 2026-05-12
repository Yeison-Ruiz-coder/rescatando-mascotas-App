package com.example.rescatando_mascotas_forever.data.network.models

import com.google.gson.annotations.SerializedName

data class Rescate(
    val id: Int? = null,
    val descripcion: String,
    val ubicacion: String,
    val estado: String, // Estado del proceso (Pendiente, En camino, etc)
    @SerializedName("clasificacion")
    val clasificacion: String, // Urgente, Herido, Abandonado, Otro
    @SerializedName("fecha_rescate")
    val fechaRescate: String,
    val foto: String? = null,
    @SerializedName("nombre_reportante")
    val nombreReportante: String? = null,
    @SerializedName("email_reportante")
    val emailReportante: String? = null,
    @SerializedName("telefono_reportante")
    val telefonoReportante: String? = null,
    val latitud: Double? = null,
    val longitud: Double? = null,
    @SerializedName("created_at")
    val createdAt: String? = null
)
