package com.example.rescatando_mascotas_forever.data.network.models

import com.google.gson.annotations.SerializedName

data class Mascota(
    val id: Int,
    @SerializedName("nombre_mascota")
    val nombre: String,
    val especie: String,
    @SerializedName("edad_aprox")
    val edadAprox: Double,
    val genero: String,
    val estado: String,
    val descripcion: String?,
    @SerializedName("lugar_rescate")
    val ubicacion: String,
    @SerializedName("foto_principal")
    val fotoPrincipal: String?,
    @SerializedName("apto_con_ninos")
    val aptoConNinos: Boolean = true,
    @SerializedName("apto_con_otros_animales")
    val aptoConOtrosAnimales: Boolean = true,
    @SerializedName("fundacion_id")
    val fundacionId: Int
)

data class MascotaResponse(
    val data: List<Mascota>
)
