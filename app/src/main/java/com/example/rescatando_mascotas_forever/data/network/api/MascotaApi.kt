package com.example.rescatando_mascotas_forever.data.network.api

import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.data.network.models.MascotaResponse
import com.example.rescatando_mascotas_forever.data.network.models.MascotaDetailResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface MascotaApi {

    // --- RUTAS PÚBLICAS ---
    @GET("api/mascotas")
    suspend fun getMascotas(
        @Query("especie") especie: String? = null,
        @Query("estado") estado: String? = null,
        @Query("page") page: Int? = null
    ): MascotaResponse

    @GET("api/mascotas/{id}")
    suspend fun getMascotaById(
        @Path("id") id: Int
    ): MascotaDetailResponse

    // --- RUTAS ADMINISTRATIVAS ---
    @GET("api/admin/mascotas")
    suspend fun getAdminMascotas(): MascotaResponse

    @Multipart
    @POST("api/admin/mascotas")
    suspend fun adminStoreMascota(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part fotoPrincipal: MultipartBody.Part? = null,
        @Part galeria: List<MultipartBody.Part>? = null
    ): MascotaResponse

    @Multipart
    @POST("api/admin/mascotas/{id}")
    suspend fun adminUpdateMascota(
        @Path("id") id: Int,
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part fotoPrincipal: MultipartBody.Part? = null,
        @Part galeria: List<MultipartBody.Part>? = null
    ): MascotaResponse

    @DELETE("api/admin/mascotas/{id}")
    suspend fun adminDeleteMascota(@Path("id") id: Int): MascotaResponse

    // --- RUTAS DE ENTIDAD (FUNDACIÓN/VETERINARIA) ---
    @GET("api/entity/mascotas")
    suspend fun getMisMascotas(): MascotaResponse

    @Multipart
    @POST("api/entity/mascotas")
    suspend fun storeMascota(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part fotoPrincipal: MultipartBody.Part? = null,
        @Part galeria: List<MultipartBody.Part>? = null
    ): MascotaResponse

    @GET("api/entity/mascotas/{id}")
    suspend fun getMascotaDetalleEntity(@Path("id") id: Int): Mascota

    @Multipart
    @POST("api/entity/mascotas/{id}")
    suspend fun updateMascota(
        @Path("id") id: Int,
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part fotoPrincipal: MultipartBody.Part? = null,
        @Part galeria: List<MultipartBody.Part>? = null
    ): MascotaResponse

    @DELETE("api/entity/mascotas/{id}")
    suspend fun deleteMascota(@Path("id") id: Int): MascotaResponse
}
