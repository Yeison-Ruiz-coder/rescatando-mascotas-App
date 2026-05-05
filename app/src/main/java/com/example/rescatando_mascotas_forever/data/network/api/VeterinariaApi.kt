package com.example.rescatando_mascotas_forever.data.network.api

import com.example.rescatando_mascotas_forever.data.network.models.Veterinaria
import com.example.rescatando_mascotas_forever.data.network.models.VeterinariaResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VeterinariaApi {

    @GET("api/veterinarias")
    suspend fun getVeterinarias(
        @Query("page") page: Int? = null
    ): VeterinariaResponse

    @GET("api/veterinarias/{id}")
    suspend fun getVeterinariaById(
        @Path("id") id: Int
    ): Veterinaria // Or VeterinariaResponse if the detail is also wrapped
}
