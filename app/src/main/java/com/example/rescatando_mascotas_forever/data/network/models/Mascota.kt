package com.example.rescatando_mascotas_forever.data.network.models

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class Mascota(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("nombre_mascota")
    val nombre: String,

    @SerializedName("especie")
    val especie: String? = null,

    @SerializedName("edad_aprox")
    val edadAprox: Double? = null,

    @SerializedName("peso_aprox")
    val pesoAprox: Double? = null,

    @SerializedName("tamano")
    val tamano: String? = null, 

    @SerializedName("color")
    val color: String? = null,

    @SerializedName("genero")
    val genero: String? = null, 

    @SerializedName("estado")
    val estado: String? = "En adopcion", 

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("lugar_rescate")
    val ubicacion: String? = null,

    @SerializedName("condiciones_especiales")
    val condicionesEspeciales: String? = null,

    @SerializedName("salud_general")
    val saludGeneral: String? = null,

    @SerializedName("esterilizado")
    val esterilizado: Boolean = false,

    @SerializedName("desparasitado")
    val desparasitado: Boolean = false,

    @SerializedName("vacunado")
    val vacunado: Boolean = false,

    @SerializedName("foto_principal")
    val fotoPrincipal: String? = null,

    @SerializedName("galeria_fotos")
    val galeriaFotos: JsonElement? = null, // Usamos JsonElement para que no importe si es String o Array

    @SerializedName("necesita_hogar_temporal")
    val necesitaHogarTemporal: Boolean = false,

    @SerializedName("apto_con_ninos")
    val aptoConNinos: Boolean = true,

    @SerializedName("apto_con_otros_animales")
    val aptoConOtrosAnimales: Boolean = true,

    @SerializedName("fundacion_id")
    val fundacionId: Int? = null,

    @SerializedName("veterinaria_id")
    val veterinariaId: Int? = null
)

data class MascotaResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: Any?
)

data class MascotaDataWrapper(
    @SerializedName("current_page")
    val currentPage: Int?,

    @SerializedName("data")
    val data: List<Mascota>
)
