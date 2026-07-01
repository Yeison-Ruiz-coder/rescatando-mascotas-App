package com.example.rescatando_mascotas_forever.data.network.api

import com.example.rescatando_mascotas_forever.data.network.models.ApiResponse
import com.example.rescatando_mascotas_forever.data.network.models.PaginatedResponse
import com.example.rescatando_mascotas_forever.data.network.models.Rescate
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import okhttp3.MultipartBody
import retrofit2.http.*

interface EntityApi {

    // --- RESCATES ---
    @GET("api/entity/rescates/disponibles")
    suspend fun getRescatesDisponibles(@Query("page") page: Int = 1): ApiResponse<Any>

    @GET("api/entity/rescates/mis-rescates")
    suspend fun getMisRescates(@Query("page") page: Int = 1): ApiResponse<Any>

    @GET("api/entity/rescates/{id}")
    suspend fun getRescateById(@Path("id") id: Int): ApiResponse<Any>

    @PUT("api/entity/rescates/{id}/aceptar")
    suspend fun aceptarRescate(@Path("id") id: Int): ApiResponse<Any>

    @PUT("api/entity/rescates/{id}/rechazar")
    suspend fun rechazarRescate(@Path("id") id: Int): ApiResponse<Any>

    @PUT("api/entity/rescates/{id}/completar")
    suspend fun completarRescate(@Path("id") id: Int): ApiResponse<Any>

    @Multipart
    @POST("api/entity/rescates/{id}/registrar-mascota")
    suspend fun registrarMascotaDesdeRescate(
        @Path("id") rescateId: Int,
        @Part parts: List<MultipartBody.Part>
    ): ApiResponse<Any>

    // --- MASCOTAS (CRUD) ---
    @GET("api/entity/mascotas")
    suspend fun getMisMascotas(
        @Query("page") page: Int = 1,
        @Query("search") search: String? = null,
        @Query("especie") especie: String? = null,
        @Query("include") include: String = "razas,vacunas"
    ): ApiResponse<Any>

    @GET("api/entity/mascotas/{id}")
    suspend fun getMascotaDetalle(
        @Path("id") id: Int,
        @Query("include") include: String = "fundacion,razas,vacunas"
    ): ApiResponse<Any>

    @Multipart
    @POST("api/entity/mascotas")
    suspend fun crearMascota(@Part parts: List<MultipartBody.Part>): ApiResponse<Any>

    @Multipart
    @POST("api/entity/mascotas/{id}")
    suspend fun actualizarMascota(@Path("id") id: Int, @Part parts: List<MultipartBody.Part>): ApiResponse<Any>

    @DELETE("api/entity/mascotas/{id}")
    suspend fun eliminarMascota(@Path("id") id: Int): ApiResponse<Any>

    @GET("api/entity/mascotas-form-data")
    suspend fun getMascotasFormData(): ApiResponse<Any>
}
