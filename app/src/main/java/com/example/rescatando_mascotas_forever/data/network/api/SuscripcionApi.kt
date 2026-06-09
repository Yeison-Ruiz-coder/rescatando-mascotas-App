package com.example.rescatando_mascotas_forever.data.network.api

import com.example.rescatando_mascotas_forever.data.network.models.SingleSuscripcionResponse
import com.example.rescatando_mascotas_forever.data.network.models.SuscripcionResponse
import retrofit2.http.*

interface SuscripcionApi {
    // Rutas con autenticación (User)
    @GET("api/suscripciones/user/mis-suscripciones")
    suspend fun getMisSuscripciones(): SuscripcionResponse

    @POST("api/suscripciones/user/crear")
    suspend fun createSuscripcion(@Body suscripcion: Map<String, Any>): SingleSuscripcionResponse

    @GET("api/suscripciones/user/{id}")
    suspend fun findSuscripcion(@Path("id") id: Int): SingleSuscripcionResponse

    @PATCH("api/suscripciones/user/{id}/cancelar")
    suspend fun cancelarSuscripcion(@Path("id") id: Int): SuscripcionResponse

    @PATCH("api/suscripciones/user/{id}/pausar")
    suspend fun pausarSuscripcion(@Path("id") id: Int): SuscripcionResponse

    @PATCH("api/suscripciones/user/{id}/reactivar")
    suspend fun reactivarSuscripcion(@Path("id") id: Int): SuscripcionResponse

    // Rutas Públicas - Planes
    @GET("api/suscripciones/planes")
    suspend fun getPlanes(): SuscripcionResponse

    @GET("api/suscripciones/planes/{id}")
    suspend fun planDetalle(@Path("id") id: Int): SingleSuscripcionResponse
}
