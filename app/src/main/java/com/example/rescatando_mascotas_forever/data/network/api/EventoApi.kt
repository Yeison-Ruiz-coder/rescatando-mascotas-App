package com.example.rescatando_mascotas_forever.data.network.api

import com.example.rescatando_mascotas_forever.data.network.models.EventoResponse
import retrofit2.http.GET

interface EventoApi {
    @GET("api/eventos")
    suspend fun getEventos(): EventoResponse
}
