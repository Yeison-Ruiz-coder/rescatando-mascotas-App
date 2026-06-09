package com.example.rescatando_mascotas_forever.data.network.models

import com.google.gson.annotations.SerializedName

data class Suscripcion(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("nombre_plan")
    val nombrePlan: String,
    @SerializedName("precio")
    val precio: Double,
    @SerializedName("descripcion")
    val descripcion: String,
    @SerializedName("beneficios")
    val beneficios: List<String>,
    @SerializedName("color_hex")
    val colorHex: String? = "#673AB7"
)

data class UserSuscripcion(
    @SerializedName("id")
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("plan_id")
    val planId: Int,
    @SerializedName("fecha_inicio")
    val fechaInicio: String,
    @SerializedName("fecha_fin")
    val fechaFin: String,
    @SerializedName("estado")
    val estado: String // "Activa", "Expirada", "Cancelada"
)
