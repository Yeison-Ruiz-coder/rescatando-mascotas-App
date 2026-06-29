package com.example.rescatando_mascotas_forever.data.network.api

import com.example.rescatando_mascotas_forever.data.network.models.ApiResponse
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

data class MonthlyData(val label: String, val value: Float)

data class SpeciesDistribution(val name: String, val count: Int, val color: String)

data class AdminStatsResponse(
    @SerializedName("total_mascotas") val totalMascotas: Int,
    @SerializedName("mascotas_trend") val mascotasTrend: String? = "0%",
    @SerializedName("total_rescates") val totalRescates: Int,
    @SerializedName("rescates_trend") val rescatesTrend: String? = "0%",
    @SerializedName("total_adopciones") val totalAdopciones: Int,
    @SerializedName("adopciones_trend") val adopcionesTrend: String? = "0%",
    @SerializedName("total_usuarios") val totalUsuarios: Int,
    @SerializedName("total_suscripciones") val totalSuscripciones: Int? = 0,
    @SerializedName("adoptions_history") val adoptionsHistory: List<MonthlyData>? = emptyList(),
    @SerializedName("rescue_history") val rescueHistory: List<MonthlyData>? = emptyList(),
    @SerializedName("species_distribution") val speciesDistribution: List<SpeciesDistribution>? = emptyList()
)

interface AdminApi {
    @GET("api/admin/dashboard")
    suspend fun getGeneralStats(): ApiResponse<AdminStatsResponse>
}
