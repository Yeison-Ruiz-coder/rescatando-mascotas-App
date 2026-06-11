package com.example.rescatando_mascotas_forever.data.network.models

import com.google.gson.annotations.SerializedName

data class Veterinaria(
    @SerializedName("id")
    val id: Int,

    @SerializedName("Nombre_vet")
    val nombreVet: String,

    @SerializedName("descripcion")
    val descripcion: String?,

    @SerializedName("anios_experiencia")
    val aniosExperiencia: Int?,

    @SerializedName("Direccion")
    val direccion: String?,

    @SerializedName("ciudad")
    val ciudad: String?,

    @SerializedName("departamento")
    val departamento: String?,

    @SerializedName("Telefono")
    val telefono: String?,

    @SerializedName("whatsapp")
    val whatsapp: String?,

    @SerializedName("redes_sociales")
    val redesSociales: Any?,

    @SerializedName("Email")
    val email: String?,

    @SerializedName("sitio_web")
    val sitioWeb: String?,

    @SerializedName("logo")
    val logo: String?,

    @SerializedName("galeria_fotos")
    val galeriaFotos: Any?,

    @SerializedName("logo_public_id")
    val logoPublicId: String?,

    @SerializedName("servicios")
    val servicios: Any?,

    @SerializedName("precio_consulta")
    val precioConsulta: String?,

    @SerializedName("acepta_seguros")
    val aceptaSeguros: Boolean?,

    @SerializedName("servicios_detallados")
    val serviciosDetallados: Any?,

    @SerializedName("equipo_medico")
    val equipoMedico: Any?,

    @SerializedName("horario_atencion")
    val horarioAtencion: String?,

    @SerializedName("urgencias_24h")
    val urgencias24h: Boolean?,

    @SerializedName("verificado")
    val verificado: Boolean?,

    @SerializedName("documentos_verificacion")
    val documentosVerificacion: Any?,

    @SerializedName("convenios")
    val convenios: Any?,

    @SerializedName("valoracion_promedio")
    val valoracionPromedio: String?,

    @SerializedName("total_valoraciones")
    val totalValoraciones: Int?,

    @SerializedName("user_id")
    val userId: Int?,

    @SerializedName("lat")
    val lat: String?,

    @SerializedName("lng")
    val lng: String?,

    @SerializedName("radio_atencion")
    val radioAtencion: String?,

    @SerializedName("cobertura_zona")
    val coberturaZona: Any?,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("updated_at")
    val updatedAt: String?,

    @SerializedName("deleted_at")
    val deletedAt: String?
)

data class VeterinariaResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: VeterinariaDataWrapper
)

data class VeterinariaDataWrapper(
    @SerializedName("current_page")
    val currentPage: Int?,
    @SerializedName("data")
    val data: List<Veterinaria>
)
