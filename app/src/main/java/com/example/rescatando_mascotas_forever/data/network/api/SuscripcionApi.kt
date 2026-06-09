package com.example.rescatando_mascotas_forever.data.network.api

import com.example.rescatando_mascotas_forever.data.network.models.Suscripcion
import com.example.rescatando_mascotas_forever.data.network.models.UserSuscripcion
import retrofit2.http.*

interface SuscripcionApi {

    @GET("api/v1/suscripciones/planes")
    suspend fun getPlanesSuscripcion(): List<Suscripcion>

    @POST("api/v1/suscripciones/suscribirse")
    suspend fun suscribirse(@Body planId: Int): UserSuscripcion

    @GET("api/v1/suscripciones/mis-suscripciones")
    suspend fun getMisSuscripciones(): List<UserSuscripcion>
}
