package com.example.rescatando_mascotas_forever.data.network.models

import com.google.gson.annotations.SerializedName

data class Mascota(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre_mascota")
    val nombre: String,

    @SerializedName("especie")
    val especie: String,

    @SerializedName("edad_aprox")
    val edadAprox: Double?,

    @SerializedName("genero")
    val genero: String,

    @SerializedName("estado")
    val estado: String,

    @SerializedName("descripcion")
    val descripcion: String?,

    @SerializedName("lugar_rescate")
    val ubicacion: String?,

    @SerializedName("foto_principal")
    val fotoPrincipal: String?,

    @SerializedName("apto_con_ninos")
    val aptoConNinos: Boolean,

    @SerializedName("apto_con_otros_animales")
    val aptoConOtrosAnimales: Boolean,

    @SerializedName("fundacion_id")
    val fundacionId: Int?
)

data class MascotaResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: MascotaDataWrapper
)

data class MascotaDataWrapper(
    @SerializedName("current_page")
    val currentPage: Int?,

    @SerializedName("data")
    val data: List<Mascota>
)