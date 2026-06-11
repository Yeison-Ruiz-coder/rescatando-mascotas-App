package com.example.rescatando_mascotas_forever.data.network.api

import com.example.rescatando_mascotas_forever.data.network.models.Rescate
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RescateApi {

    @GET("api/v1/rescates")
    suspend fun getRescates(): List<Rescate>

    @POST("api/v1/rescates")
    suspend fun createRescate(
        @Body rescate: Rescate
    ): Rescate

    @GET("api/v1/rescates/{id}")
    suspend fun getRescateById(
        @Path("id") id: Int
    ): Rescate
}
