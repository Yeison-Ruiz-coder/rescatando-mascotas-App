package com.example.rescatando_mascotas_forever.data.repository

import com.example.rescatando_mascotas_forever.data.network.api.EntityApi
import com.example.rescatando_mascotas_forever.data.network.models.ApiResponse
import com.example.rescatando_mascotas_forever.data.network.models.PaginatedResponse
import com.example.rescatando_mascotas_forever.data.network.models.Rescate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FoundationRescatesRepository(private val api: EntityApi) {

    private val gson = Gson()

    suspend fun getRescatesDisponibles(page: Int = 1): ApiResponse<PaginatedResponse<Rescate>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getRescatesDisponibles(page)
                processPaginatedResponse(response)
            } catch (e: Exception) {
                ApiResponse(false, null, e.message)
            }
        }
    }

    suspend fun getMisRescates(page: Int = 1): ApiResponse<PaginatedResponse<Rescate>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getMisRescates(page)
                processPaginatedResponse(response)
            } catch (e: Exception) {
                ApiResponse(false, null, e.message)
            }
        }
    }

    private fun processPaginatedResponse(response: ApiResponse<Any>): ApiResponse<PaginatedResponse<Rescate>> {
        return if (response.success) {
            if (response.data is List<*>) {
                ApiResponse(true, PaginatedResponse(emptyList(), 1, 1, 0), response.message)
            } else if (response.data != null) {
                val json = gson.toJson(response.data)
                val type = object : TypeToken<PaginatedResponse<Rescate>>() {}.type
                ApiResponse(true, gson.fromJson(json, type), response.message)
            } else {
                ApiResponse(true, PaginatedResponse(emptyList(), 1, 1, 0), response.message)
            }
        } else {
            ApiResponse(false, null, response.message)
        }
    }

    suspend fun getRescateById(id: Int): ApiResponse<Rescate> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getRescateById(id)
                processSingleRescateResponse(response)
            } catch (e: Exception) {
                ApiResponse(false, null, e.message)
            }
        }
    }

    suspend fun aceptarRescate(id: Int): ApiResponse<Rescate> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.aceptarRescate(id)
                processSingleRescateResponse(response)
            } catch (e: Exception) {
                ApiResponse(false, null, e.message)
            }
        }
    }

    suspend fun rechazarRescate(id: Int): ApiResponse<Rescate> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.rechazarRescate(id)
                processSingleRescateResponse(response)
            } catch (e: Exception) {
                ApiResponse(false, null, e.message)
            }
        }
    }

    suspend fun completarRescate(id: Int): ApiResponse<Rescate> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.completarRescate(id)
                processSingleRescateResponse(response)
            } catch (e: Exception) {
                ApiResponse(false, null, e.message)
            }
        }
    }

    private fun processSingleRescateResponse(response: ApiResponse<Any>): ApiResponse<Rescate> {
        return if (response.success && response.data != null && response.data !is List<*>) {
            val json = gson.toJson(response.data)
            ApiResponse(true, gson.fromJson(json, Rescate::class.java), response.message)
        } else {
            ApiResponse(response.success, null, response.message)
        }
    }
}
