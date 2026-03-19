package com.example.rescatando_mascotas_forever.data.network.api

import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.data.network.models.MascotaResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MascotaApi {

    @GET("api/v1/mascotas")
    suspend fun getMascotas(
        @Query("especie") especie: String? = null,
        @Query("estado") estado: String? = null,
        @Query("page") page: Int? = null
    ): MascotaResponse

    @GET("api/v1/mascotas/{id}")
    suspend fun getMascotaById(
        @Path("id") id: Int
    ): Mascota
}