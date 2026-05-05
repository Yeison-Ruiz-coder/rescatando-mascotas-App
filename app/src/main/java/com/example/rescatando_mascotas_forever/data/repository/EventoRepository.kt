package com.example.rescatando_mascotas_forever.data.repository

import com.example.rescatando_mascotas_forever.data.network.api.EventoApi
import com.example.rescatando_mascotas_forever.data.network.models.Evento
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EventoRepository(private val api: EventoApi) {

    fun getEventos(): Flow<Result<List<Evento>>> = flow {
        try {
            val response = api.getEventos()
            if (response.success) {
                emit(Result.success(response.data))
            } else {
                emit(Result.failure(Exception("Error al obtener eventos")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
