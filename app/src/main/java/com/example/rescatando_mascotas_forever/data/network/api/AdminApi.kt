package com.example.rescatando_mascotas_forever.data.network.api

import retrofit2.http.GET

data class MonthlyData(val label: String, val value: Float)

data class SpeciesDistribution(val name: String, val count: Int, val color: String)

data class AdminStatsResponse(
    val totalMascotas: Int,
    val mascotasTrend: String,
    val totalRescates: Int,
    val rescatesTrend: String,
    val totalAdopciones: Int,
    val adopcionesTrend: String,
    val totalUsuarios: Int, // Verificamos que este campo exista
    val adoptionsHistory: List<MonthlyData>,
    val rescueHistory: List<MonthlyData>,
    val speciesDistribution: List<SpeciesDistribution>
)

interface AdminApi {
    @GET("api/admin/stats")
    suspend fun getGeneralStats(): AdminStatsResponse
}
