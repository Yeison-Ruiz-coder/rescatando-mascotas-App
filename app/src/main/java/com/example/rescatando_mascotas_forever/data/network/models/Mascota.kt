package com.example.rescatando_mascotas_forever.data.network.models

import com.google.gson.annotations.SerializedName

data class Mascota(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("nombre_mascota")
    val nombre: String,

    @SerializedName("especie")
    val especie: String? = null,

    @SerializedName("edad_aprox")
    val edadAprox: Any? = null,

    @SerializedName("peso_aprox")
    val pesoAprox: Any? = null,

    @SerializedName("tamano")
    val tamano: String? = null,

    @SerializedName("color")
    val color: String? = null,

    @SerializedName("genero")
    val genero: String? = null,

    @SerializedName("estado")
    val estado: String? = "En adopcion",

    @SerializedName("lugar_rescate")
    val ubicacion: String? = null,

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("condiciones_especiales")
    val condicionesEspeciales: Any? = null,

    @SerializedName("salud_general")
    val saludGeneral: Any? = null,

    @SerializedName("esterilizado")
    val esterilizado: Any? = null,

    @SerializedName("desparasitado")
    val desparasitado: Any? = null,

    @SerializedName("vacunado")
    val vacunado: Any? = null,

    @SerializedName("enfermedades_cronicas")
    val enfermedadesCronicas: Any? = null,

    @SerializedName("medicamentos")
    val medicamentos: Any? = null,

    @SerializedName("foto_principal")
    val fotoPrincipal: String? = null,

    @SerializedName("foto_principal_public_id")
    val fotoPrincipalPublicId: String? = null,

    @SerializedName("galeria_fotos")
    val galeriaFotos: Any? = null,

    @SerializedName("video_url")
    val videoUrl: String? = null,

    @SerializedName("video_public_id")
    val videoPublicId: String? = null,

    @SerializedName("necesita_hogar_temporal")
    val necesitaHogarTemporal: Any? = null,

    @SerializedName("apto_con_ninos")
    val aptoConNinos: Any? = null,

    @SerializedName("apto_con_otros_animales")
    val aptoConOtrosAnimales: Any? = null,

    @SerializedName("requisitos_adopcion")
    val requisitosAdopcion: Any? = null,

    @SerializedName("hogar_recomendado")
    val hogarRecomendado: Any? = null,

    @SerializedName("fundacion_id")
    val fundacionId: Int? = null,

    @SerializedName("veterinaria_id")
    val veterinariaId: Int? = null,

    @SerializedName("destacada")
    val destacada: Any? = null,

    @SerializedName("fundacion")
    val fundacion: Fundacion? = null
) {
    fun isEsterilizado(): Boolean = toBoolean(esterilizado)
    fun isDesparasitado(): Boolean = toBoolean(desparasitado)
    fun isVacunado(): Boolean = toBoolean(vacunado)
    fun isAptoNinos(): Boolean = toBoolean(aptoConNinos)
    fun isAptoOtrosAnimales(): Boolean = toBoolean(aptoConOtrosAnimales)
    fun isHogarTemporal(): Boolean = toBoolean(necesitaHogarTemporal)
    fun isDestacada(): Boolean = toBoolean(destacada)

    private fun toBoolean(value: Any?): Boolean {
        return when (value) {
            is Boolean -> value
            is Number -> value.toInt() == 1
            is String -> value == "1" || value.lowercase() == "true"
            else -> false
        }
    }
}

fun Any?.toSafeInt(): Int {
    return when (this) {
        is Number -> this.toInt()
        is String -> this.toDoubleOrNull()?.toInt() ?: 0
        else -> 0
    }
}

fun Any?.toSafeString(): String {
    return when (this) {
        null -> ""
        is String -> this
        is List<*> -> this.joinToString(", ")
        else -> this.toString()
    }
}

data class Fundacion(
    val id: Int,
    @SerializedName("Nombre_1")
    val nombre: String,
    @SerializedName("imagen_portada")
    val imagenPortada: String? = null,
    val ciudad: String? = null
)

data class MascotaResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: PaginatedResponse<Mascota>?
)

data class MascotaDetailResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: Mascota?
)
