package com.example.rescatando_mascotas_forever.data.network.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

// Modelos de respuesta para las métricas de Railway
data class AdminStatsResponse(
    val totalMascotas: Int,
    val totalRescates: Int,
    val totalAdopciones: Int,
    val totalUsuarios: Int
)

data class ReporteItem(
    val id: Int,
    val tipoReporte: String,
    val descripcion: String,
    val fecha: String,
    val estado: String
)

interface AdminApi {
    @GET("api/admin/stats")
    suspend fun getGeneralStats(): AdminStatsResponse

    @GET("api/admin/reportes")
    suspend fun getReportesPlataforma(@Query("tipo") tipo: String?): List<ReporteItem>

    @PUT("api/admin/usuarios/{id}/rol")
    suspend fun cambiarRolUsuario(@Path("id") id: Int, @Query("nuevoRol") nuevoRol: String): Response<Unit>
}
