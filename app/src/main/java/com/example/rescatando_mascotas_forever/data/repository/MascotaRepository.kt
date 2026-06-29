package com.example.rescatando_mascotas_forever.data.repository

import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.data.network.models.MascotaResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MascotaRepository {

    private val api = RetrofitClient.mascotaApi

    fun getMascotas(
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

    fun getMascotaById(id: Int): Flow<Result<MascotaResponse>> = flow {
        try {
            val response = api.getMascotaById(id)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    // --- MÉTODOS PARA ENTIDADES (FUNDACIÓN/VETERINARIA) ---

    fun getMisMascotas(): Flow<Result<MascotaResponse>> = flow {
        try {
            val response = api.getMisMascotas()
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun storeMascota(
        partMap: Map<String, RequestBody>,
        fotoPrincipal: MultipartBody.Part?,
        galeria: List<MultipartBody.Part>?
    ): Result<MascotaResponse> {
        return try {
            val response = api.storeMascota(partMap, fotoPrincipal, galeria)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateMascota(
        id: Int,
        partMap: Map<String, RequestBody>,
        fotoPrincipal: MultipartBody.Part?,
        galeria: List<MultipartBody.Part>?
    ): Result<MascotaResponse> {
        return try {
            val response = api.updateMascota(id, partMap, fotoPrincipal, galeria)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteMascota(id: Int): Result<MascotaResponse> {
        return try {
            val response = api.deleteMascota(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
