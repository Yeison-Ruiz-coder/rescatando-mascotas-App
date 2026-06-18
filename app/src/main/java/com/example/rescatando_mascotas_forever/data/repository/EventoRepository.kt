package com.example.rescatando_mascotas_forever.data.repository

import com.example.rescatando_mascotas_forever.data.network.api.EventoApi
import com.example.rescatando_mascotas_forever.data.network.models.Evento
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EventoRepository(private val api: EventoApi) {

    fun getEventos(): Flow<Result<List<Evento>>> = flow {
        try {
            val response = api.getEventos()
            if (response.success && response.data != null) {
                // Sacamos la lista del objeto de paginación
                emit(Result.success(response.data.data))
            } else {
                emit(Result.failure(Exception("El servidor no devolvió eventos validos")))
            }
        } catch (e: Exception) {
            println("DEBUG_REPOSITORY: Error en repo: ${e.message}")
            emit(Result.failure(e))
        }
    }
}
