package com.example.rescatando_mascotas_forever.data.network.api

import com.example.rescatando_mascotas_forever.data.network.models.ApiResponse
import com.example.rescatando_mascotas_forever.data.network.models.Rescate
import okhttp3.RequestBody
import retrofit2.http.*

interface RescateApi {

    @GET("api/rescates")
    suspend fun getRescates(): List<Rescate>

    @POST("api/rescates/reportar")
    suspend fun reportarRescate(
        @Body body: RequestBody
    ): ApiResponse<Rescate>

    @GET("api/rescates/{id}")
    suspend fun getRescateById(
        @Path("id") id: Int
    ): Rescate
}
