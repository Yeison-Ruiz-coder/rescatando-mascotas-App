package com.example.rescatando_mascotas_forever.data.repository

import com.example.rescatando_mascotas_forever.data.network.api.RetrofitClient
import com.example.rescatando_mascotas_forever.data.network.models.Veterinaria
import com.example.rescatando_mascotas_forever.data.network.models.VeterinariaResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VeterinariaRepository {

    private val api = RetrofitClient.veterinariaApi

    suspend fun getVeterinarias(page: Int? = null): Flow<Result<VeterinariaResponse>> = flow {
        try {
            val response = api.getVeterinarias(page)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun getVeterinariaById(id: Int): Flow<Result<Veterinaria>> = flow {
        try {
            val response = api.getVeterinariaById(id)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
