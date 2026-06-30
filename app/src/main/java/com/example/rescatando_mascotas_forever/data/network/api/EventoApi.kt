package com.example.rescatando_mascotas_forever.data.network.api

import com.example.rescatando_mascotas_forever.data.network.models.EventoResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface EventoApi {
    @GET("api/eventos")
    suspend fun getEventos(
        @Query("page") page: Int? = null
    ): EventoResponse

    @Multipart
    @POST("api/v1/admin/eventos")
    suspend fun crearEvento(
        @Part("nombre_evento") nombre: RequestBody,
        @Part("lugar_evento") lugar: RequestBody,
        @Part("fecha_evento") fecha: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("tipo") tipo: RequestBody,
        @Part foto_principal: MultipartBody.Part?
    ): EventoResponse

    @Multipart
    @POST("api/v1/admin/eventos/{id}")
    suspend fun actualizarEvento(
        @Path("id") id: Int,
        @Part("_method") method: RequestBody, 
        @Part("nombre_evento") nombre: RequestBody,
        @Part("lugar_evento") lugar: RequestBody,
        @Part("fecha_evento") fecha: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("tipo") tipo: RequestBody,
        @Part foto_principal: MultipartBody.Part?
    ): EventoResponse

    @FormUrlEncoded
    @POST("api/v1/admin/eventos/{id}")
    suspend fun eliminarEvento(
        @Path("id") id: Int,
        @Field("_method") method: String = "DELETE"
    ): EventoResponse
}
