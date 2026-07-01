package com.example.rescatando_mascotas_forever.data.repository

import com.example.rescatando_mascotas_forever.data.network.api.EntityApi
import com.example.rescatando_mascotas_forever.data.network.models.ApiResponse
import com.example.rescatando_mascotas_forever.data.network.models.PaginatedResponse
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MultipartBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FoundationMascotasRepository(private val api: EntityApi) {

    private val gson = Gson()

    suspend fun getMascotas(page: Int = 1, search: String? = null, especie: String? = null): ApiResponse<PaginatedResponse<Mascota>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getMisMascotas(page, search, especie)
                
                if (response.success) {
                    if (response.data == null || (response.data is List<*> && response.data.isEmpty())) {
                        return@withContext ApiResponse(true, PaginatedResponse(emptyList(), 1, 1, 0), response.message)
                    }

                    val json = gson.toJson(response.data)
                    if (response.data is List<*>) {
                        val type = object : TypeToken<List<Mascota>>() {}.type
                        val list: List<Mascota> = gson.fromJson(json, type) ?: emptyList()
                        return@withContext ApiResponse(true, PaginatedResponse(list, 1, 1, list.size), response.message)
                    } 
                    
                    val type = object : TypeToken<PaginatedResponse<Mascota>>() {}.type
                    ApiResponse(true, gson.fromJson(json, type), response.message)
                } else {
                    ApiResponse(false, null, response.message)
                }
            } catch (e: Exception) {
                ApiResponse(false, null, e.message)
            }
        }
    }

    suspend fun getMascotaDetalle(id: Int): ApiResponse<Mascota> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getMascotaDetalle(id)
                if (response.success && response.data != null && response.data !is List<*>) {
                    val json = gson.toJson(response.data)
                    ApiResponse(true, gson.fromJson(json, Mascota::class.java), response.message)
                } else {
                    ApiResponse(false, null, response.message ?: "Mascota no encontrada")
                }
            } catch (e: Exception) {
                ApiResponse(false, null, e.message)
            }
        }
    }

    suspend fun getMascotasFormData(): ApiResponse<Map<String, Any>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getMascotasFormData()
                if (response.success && response.data is Map<*, *>) {
                    ApiResponse(true, response.data as Map<String, Any>, response.message)
                } else {
                    ApiResponse(true, emptyMap<String, Any>(), response.message)
                }
            } catch (e: Exception) {
                ApiResponse(false, emptyMap(), e.message)
            }
        }
    }

    suspend fun saveMascota(
        id: Int?,
        fields: Map<String, String>,
        arrays: Map<String, List<String>>,
        fotoPrincipal: MultipartBody.Part?,
        galeria: List<MultipartBody.Part>?,
        rescateId: Int? = null
    ): ApiResponse<Mascota> {
        return withContext(Dispatchers.IO) {
            try {
                val parts = mutableListOf<MultipartBody.Part>()
                fields.forEach { (key, value) -> parts.add(MultipartBody.Part.createFormData(key, value)) }
                arrays.forEach { (key, list) -> list.forEach { value -> parts.add(MultipartBody.Part.createFormData("$key[]", value)) } }
                fotoPrincipal?.let { parts.add(it) }
                galeria?.forEach { parts.add(it) }

                val response = when {
                    rescateId != null -> api.registrarMascotaDesdeRescate(rescateId, parts)
                    id != null -> {
                        parts.add(MultipartBody.Part.createFormData("_method", "PUT"))
                        api.actualizarMascota(id, parts)
                    }
                    else -> api.crearMascota(parts)
                }

                if (response.success && response.data != null && response.data !is List<*>) {
                    val json = gson.toJson(response.data)
                    ApiResponse(true, gson.fromJson(json, Mascota::class.java), response.message)
                } else {
                    ApiResponse(response.success, null, response.message)
                }
            } catch (e: Exception) {
                ApiResponse(false, null, e.message)
            }
        }
    }

    suspend fun eliminarMascota(id: Int): ApiResponse<Any> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.eliminarMascota(id)
                // Si llegamos aquí y success es true, lo damos por bueno
                ApiResponse(response.success, response.data, response.message)
            } catch (e: Exception) {
                ApiResponse(false, null, e.message)
            }
        }
    }
}
