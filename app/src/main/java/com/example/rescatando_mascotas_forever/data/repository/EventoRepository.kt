package com.example.rescatando_mascotas_forever.data.repository

import android.content.Context
import android.net.Uri
import com.example.rescatando_mascotas_forever.data.network.api.EventoApi
import com.example.rescatando_mascotas_forever.data.network.models.Evento
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class EventoRepository(private val api: EventoApi) {

    fun getEventos(page: Int? = null): Flow<Result<com.example.rescatando_mascotas_forever.data.network.models.EventoPagination>> = flow {
        try {
            val response = api.getEventos(page)
            if (response.success && response.data != null) {
                emit(Result.success(response.data))
            } else {
                emit(Result.failure(Exception("Error al cargar eventos")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun crearEvento(context: Context, evento: Evento, imageUri: Uri?): Result<Evento> {
        return try {
            val nombre = evento.nombre.toRequestBody("text/plain".toMediaTypeOrNull())
            val lugar = evento.lugar.toRequestBody("text/plain".toMediaTypeOrNull())
            val fecha = evento.fecha.toRequestBody("text/plain".toMediaTypeOrNull())
            val descripcion = (evento.descripcion ?: "").toRequestBody("text/plain".toMediaTypeOrNull())
            val tipo = (evento.tipo ?: "NORMAL").toRequestBody("text/plain".toMediaTypeOrNull())

            val imagePart = imageUri?.let { prepareImagePart(context, it, "foto_principal") }

            val response = api.crearEvento(nombre, lugar, fecha, descripcion, tipo, imagePart)
            if (response.success) Result.success(evento)
            else Result.failure(Exception(response.message))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarEvento(context: Context, id: Int, evento: Evento, imageUri: Uri?): Result<Evento> {
        return try {
            val method = "PUT".toRequestBody("text/plain".toMediaTypeOrNull())
            val nombre = evento.nombre.toRequestBody("text/plain".toMediaTypeOrNull())
            val lugar = evento.lugar.toRequestBody("text/plain".toMediaTypeOrNull())
            val fecha = evento.fecha.toRequestBody("text/plain".toMediaTypeOrNull())
            val descripcion = (evento.descripcion ?: "").toRequestBody("text/plain".toMediaTypeOrNull())
            val tipo = (evento.tipo ?: "NORMAL").toRequestBody("text/plain".toMediaTypeOrNull())

            val imagePart = imageUri?.let { 
                if (it.toString().startsWith("content://") || it.toString().startsWith("file://")) {
                    prepareImagePart(context, it, "foto_principal")
                } else null
            }

            val response = api.actualizarEvento(id, method, nombre, lugar, fecha, descripcion, tipo, imagePart)
            if (response.success) Result.success(evento)
            else Result.failure(Exception(response.message))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarEvento(id: Int): Result<Unit> {
        return try {
            val response = api.eliminarEvento(id)
            if (response.success) Result.success(Unit)
            else Result.failure(Exception(response.message))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun prepareImagePart(context: Context, uri: Uri, partName: String): MultipartBody.Part? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "temp_event_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.use { input -> outputStream.use { output -> input.copyTo(output) } }
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData(partName, file.name, requestFile)
        } catch (e: Exception) {
            null
        }
    }
}
