package com.example.rescatando_mascotas_forever.data.repository

import com.example.rescatando_mascotas_forever.data.network.api.EventoApi
import com.example.rescatando_mascotas_forever.data.network.models.Evento
import com.example.rescatando_mascotas_forever.data.network.models.EventoPagination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EventoRepository(private val api: EventoApi) {

    fun getEventos(page: Int = 1): Flow<Result<EventoPagination>> = flow {
        try {
            val response = api.getEventos(page)
            if (response.success && response.data != null) {
                emit(Result.success(response.data))
            } else {
                emit(Result.failure(Exception(response.message ?: "Error al obtener eventos")))
            }
        } catch (e: Exception) {
            println("DEBUG_REPOSITORY: Error en repo: ${e.message}")
            emit(Result.failure(e))
        }
    }
}
