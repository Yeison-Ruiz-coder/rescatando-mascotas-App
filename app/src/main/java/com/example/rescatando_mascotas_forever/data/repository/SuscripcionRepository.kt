package com.example.rescatando_mascotas_forever.data.repository

import com.example.rescatando_mascotas_forever.data.network.api.SuscripcionApi
import com.example.rescatando_mascotas_forever.data.network.models.Suscripcion
import com.google.gson.Gson
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SuscripcionRepository(private val api: SuscripcionApi) {

    private val gson = Gson()

    private fun parseSuscripciones(element: JsonElement?): List<Suscripcion> {
        if (element == null || element.isJsonNull) return emptyList()
        return try {
            val rawList = when {
                element.isJsonArray -> element.asJsonArray.toList()
                element.isJsonObject -> {
                    val obj = element.asJsonObject
                    when {
                        obj.has("data") && obj.get("data").isJsonArray -> {
                            obj.get("data").asJsonArray.toList()
                        }
                        obj.has("id") -> listOf(element)
                        else -> emptyList()
                    }
                }
                else -> emptyList()
            }
            rawList.mapNotNull { 
                try { 
                    gson.fromJson(it, Suscripcion::class.java) 
                } catch (e: Exception) { 
                    null 
                } 
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getMisSuscripciones(): Flow<Result<List<Suscripcion>>> = flow {
        try {
            val response = api.getMisSuscripciones()
            emit(Result.success(parseSuscripciones(response.data)))
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

    fun getAllSuscripciones(): Flow<Result<List<Suscripcion>>> = flow {
        try {
            val response = api.getAllSuscripcionesAdmin()
            emit(Result.success(parseSuscripciones(response.data)))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun updateSuscripcionStatus(id: Int, status: String): Flow<Result<Boolean>> = flow {
        try {
            api.adminUpdateSuscripcionStatus(id, mapOf("estado" to status))
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
