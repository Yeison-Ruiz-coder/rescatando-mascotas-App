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
    val pesoAprox: JsonElement? = null,

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
    val condicionesEspeciales: JsonElement? = null,

    @SerializedName("salud_general")
    val saludGeneral: JsonElement? = null,

    @SerializedName("esterilizado")
    val esterilizado: Boolean? = false,

    @SerializedName("desparasitado")
    val desparasitado: Boolean? = false,

    @SerializedName("vacunado")
    val vacunado: Boolean? = false,

    @SerializedName("enfermedades_cronicas")
    val enfermedadesCronicas: JsonElement? = null,

    @SerializedName("medicamentos")
    val medicamentos: JsonElement? = null,

    @SerializedName("foto_principal")
    val fotoPrincipal: String? = null,

    @SerializedName("foto_principal_public_id")
    val fotoPrincipalPublicId: String? = null,

    @SerializedName("galeria_fotos")
    val galeriaFotos: JsonElement? = null,

    @SerializedName("video_url")
    val videoUrl: String? = null,

    @SerializedName("video_public_id")
    val videoPublicId: String? = null,

    @SerializedName("necesita_hogar_temporal")
    val necesitaHogarTemporal: Boolean? = false,

    @SerializedName("apto_con_ninos")
    val aptoConNinos: Boolean? = true,

    @SerializedName("apto_con_otros_animales")
    val aptoConOtrosAnimales: Boolean? = true,

    @SerializedName("requisitos_adopcion")
    val requisitosAdopcion: JsonElement? = null,

    @SerializedName("hogar_recomendado")
    val hogarRecomendado: JsonElement? = null,

    @SerializedName("fecha_ingreso")
    val fechaIngreso: String? = null,

    @SerializedName("fecha_publicacion")
    val fechaPublicacion: String? = null,

    @SerializedName("fecha_salida")
    val fechaSalida: String? = null,

    @SerializedName("fundacion_id")
    val fundacionId: Int? = null,

    @SerializedName("veterinaria_id")
    val veterinariaId: Int? = null,

    @SerializedName("destacada")
    val destacada: Boolean? = false,

    @SerializedName("vistas")
    val vistas: JsonElement? = null,

    @SerializedName("interesados")
    val interesados: JsonElement? = null,

    @SerializedName("padrinos")
    val padrinos: JsonElement? = null,

    @SerializedName("created_by")
    val createdBy: Int? = null,

    @SerializedName("updated_by")
    val updatedBy: Int? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    @SerializedName("deleted_at")
    val deletedAt: String? = null,

    @SerializedName("fundacion")
    val fundacion: Fundacion? = null
)

data class Fundacion(
    val id: Int,
    @SerializedName("Nombre_1")
    val nombre: String,
    @SerializedName("imagen_portada")
    val imagenPortada: String? = null,
    val ciudad: String? = null
)

fun JsonElement?.toSafeString(): String {
    if (this == null || this.isJsonNull) return ""
    return try {
        if (this.isJsonArray) {
            this.asJsonArray.mapNotNull { 
                if (it.isJsonPrimitive) it.asString else null 
            }.joinToString(", ")
        } else if (this.isJsonPrimitive) {
            this.asString
        } else {
            this.toString()
        }
    } catch (e: Exception) {
        ""
    }
}

fun JsonElement?.toSafeInt(): Int {
    if (this == null || this.isJsonNull) return 0
    return try {
        if (this.isJsonPrimitive && this.asJsonPrimitive.isNumber) {
            this.asInt
        } else if (this.isJsonArray) {
            this.asJsonArray.size()
        } else {
            0
        }
    } catch (e: Exception) {
        0
    }
}

data class MascotaResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: MascotaDataWrapper?
)

data class MascotaDetailResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: Mascota?
)

data class MascotaDataWrapper(
    @SerializedName("current_page")
    val currentPage: Int?,

    @SerializedName("last_page")
    val lastPage: Int?,

    @SerializedName("data")
    val data: List<Mascota>? = null
)
