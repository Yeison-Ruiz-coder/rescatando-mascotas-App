package com.example.rescatando_mascotas_forever.data.repository

import com.example.rescatando_mascotas_forever.data.network.api.RetrofitClient
import com.example.rescatando_mascotas_forever.data.network.models.Suscripcion
import com.example.rescatando_mascotas_forever.data.network.models.UserSuscripcion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SuscripcionRepository {
    private val api = RetrofitClient.suscripcionApi

    fun getPlanesSuscripcion(): Flow<Result<List<Suscripcion>>> = flow {
        try {
            val response = api.getPlanesSuscripcion()
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun suscribirse(planId: Int): Flow<Result<UserSuscripcion>> = flow {
        try {
            val response = api.suscribirse(planId)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getMisSuscripciones(): Flow<Result<List<UserSuscripcion>>> = flow {
        try {
            val response = api.getMisSuscripciones()
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
