package com.example.rescatando_mascotas_forever.data.repository

import com.example.rescatando_mascotas_forever.data.network.api.RetrofitClient
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.data.network.models.MascotaResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MascotaRepository {

    private val api = RetrofitClient.mascotaApi

    suspend fun getMascotas(
        especie: String? = null,
        estado: String? = null
    ): Flow<Result<MascotaResponse>> = flow {
        try {
            val response = api.getMascotas(especie, estado)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun getMascotaById(id: Int): Flow<Result<Mascota>> = flow {
        try {
            val response = api.getMascotaById(id)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}