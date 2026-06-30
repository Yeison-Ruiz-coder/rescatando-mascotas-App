package com.example.rescatando_mascotas_forever.data.network.api

import com.example.rescatando_mascotas_forever.data.network.models.ApiResponse
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

data class MonthlyData(
    @SerializedName("fecha") val label: String,
    @SerializedName("total") val value: Float
)

data class SpeciesDistribution(val name: String, val count: Int, val color: String)

data class AdminStatsResponse(
    val totalMascotas: Int,
    val mascotasTrend: String,
    val totalRescates: Int,
    val rescatesTrend: String,
    val totalAdopciones: Int,
    val adopcionesTrend: String,
    val totalUsuarios: Int,
    val adoptionsHistory: List<MonthlyData>,
    val rescueHistory: List<MonthlyData>,
    val speciesDistribution: List<SpeciesDistribution>
)

interface AdminApi {
    @GET("api/admin/dashboard")
    suspend fun getGeneralStats(): ApiResponse<AdminStatsResponse>
}
