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
    @SerializedName("stats") val stats: StatsContainer,
    @SerializedName("graficos") val graficos: GraficosContainer? = null
)

data class StatsContainer(
    @SerializedName("mascotas") val mascotas: MascotasSummary,
    @SerializedName("usuarios") val usuarios: UsuariosSummary,
    @SerializedName("adopciones") val adopciones: AdopcionesSummary,
    @SerializedName("rescates") val rescates: RescatesSummary,
    @SerializedName("donaciones") val donaciones: DonacionesSummary? = null
)

data class MascotasSummary(
    @SerializedName("total") val total: Int,
    @SerializedName("en_adopcion") val enAdopcion: Int,
    @SerializedName("adoptadas") val adoptadas: Int,
    @SerializedName("rescatadas") val rescatadas: Int
)

data class UsuariosSummary(
    @SerializedName("total") val total: Int,
    @SerializedName("activos") val activos: Int,
    @SerializedName("fundaciones") val fundaciones: Int,
    @SerializedName("veterinarias") val veterinarias: Int
)

data class AdopcionesSummary(
    @SerializedName("totales") val totales: Int,
    @SerializedName("mes_actual") val mesActual: Int
)

data class RescatesSummary(
    @SerializedName("completados") val completados: Int,
    @SerializedName("activos") val activos: Int
)

data class DonacionesSummary(
    @SerializedName("totales") val totales: Int
)

data class GraficosContainer(
    @SerializedName("adopciones_por_mes") val adoptionsHistory: List<MonthlyData>? = emptyList()
)

interface AdminApi {
    @GET("api/admin/dashboard")
    suspend fun getGeneralStats(): ApiResponse<AdminStatsResponse>
}
