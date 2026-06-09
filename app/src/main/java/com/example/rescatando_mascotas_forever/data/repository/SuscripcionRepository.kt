package com.example.rescatando_mascotas_forever.data.repository

import com.example.rescatando_mascotas_forever.data.network.api.SuscripcionApi
import com.example.rescatando_mascotas_forever.data.network.models.Suscripcion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SuscripcionRepository(private val api: SuscripcionApi) {

    fun getMisSuscripciones(): Flow<Result<List<Suscripcion>>> = flow {
        try {
            val response = api.getMisSuscripciones()
            emit(Result.success(response.data))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun createSuscripcion(data: Map<String, Any>): Flow<Result<Suscripcion>> = flow {
        try {
            val response = api.createSuscripcion(data)
            emit(Result.success(response.data))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun deleteSuscripcion(id: Int): Flow<Result<Boolean>> = flow {
        try {
            api.cancelarSuscripcion(id)
            emit(Result.success(true))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun pausarSuscripcion(id: Int): Flow<Result<Boolean>> = flow {
        try {
            api.pausarSuscripcion(id)
            emit(Result.success(true))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
