package com.example.rescatando_mascotas_forever.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.rescatando_mascotas_forever.data.network.api.RescateApi
import com.example.rescatando_mascotas_forever.data.network.models.ApiResponse
import com.example.rescatando_mascotas_forever.data.network.models.Rescate
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class RescateRepository(private val api: RescateApi) {

    suspend fun reportarRescate(
        context: Context,
        lugar: String,
        descripcion: String,
        fecha: String,
        tipo: String,
        prioridad: String,
        paraFundaciones: Boolean,
        paraVeterinarias: Boolean,
        paraAdmin: Boolean,
        nombre: String?,
        email: String?,
        telefono: String?,
        lat: Double?,
        lng: Double?,
        imageUri: Uri?,
        galeriaUris: List<Uri>
    ): Result<Rescate> = withContext(Dispatchers.IO) {
        try {
            val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

            // 1. Campos Obligatorios (Valores exactos según documentación)
            builder.addFormDataPart("lugar_rescate", lugar)
            builder.addFormDataPart("descripcion_rescate", descripcion)
            
            // Formatear fecha a YYYY-MM-DD
            val fechaBackend = try {
                val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = inputFormat.parse(fecha)
                if (date != null) outputFormat.format(date) else fecha
            } catch (e: Exception) { fecha }
            builder.addFormDataPart("fecha_rescate", fechaBackend)

            // tipo_emergencia y prioridad en minúsculas
            val tipoBackend = prioridad.lowercase() // urgente, herido, abandonado, otro
            val prioridadBackend = when(tipoBackend) {
                "urgente" -> "alta"
                "herido", "abandonado" -> "media"
                else -> "baja"
            }
            builder.addFormDataPart("tipo_emergencia", tipoBackend)
            builder.addFormDataPart("prioridad", prioridadBackend)

            // Booleans como strings "true"/"false"
            builder.addFormDataPart("disponible_para_fundaciones", paraFundaciones.toString())
            builder.addFormDataPart("disponible_para_veterinarias", paraVeterinarias.toString())
            builder.addFormDataPart("disponible_para_admin", paraAdmin.toString())

            // Coordenadas
            builder.addFormDataPart("lat", (lat ?: 0.0).toString())
            builder.addFormDataPart("lng", (lng ?: 0.0).toString())

            // 2. Campos Opcionales
            nombre?.let { if (it.isNotBlank()) builder.addFormDataPart("nombre_reportante", it) }
            email?.let { if (it.isNotBlank()) builder.addFormDataPart("email_reportante", it) }
            telefono?.let { if (it.isNotBlank()) builder.addFormDataPart("telefono_reportante", it) }

            // 3. Multimedia
            imageUri?.let { uri ->
                val file = uriToFile(context, uri)
                file?.let {
                    val requestFile = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    builder.addFormDataPart("foto_principal", it.name, requestFile)
                }
            }

            galeriaUris.forEachIndexed { index, uri ->
                val file = uriToFile(context, uri)
                file?.let {
                    val requestFile = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    builder.addFormDataPart("galeria_fotos[]", it.name, requestFile)
                }
            }

            val response = api.reportarRescate(builder.build())
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Error al reportar rescate"))
            }
        } catch (e: Exception) {
            Log.e("RescateRepo", "Error: ${e.message}")
            Result.failure(e)
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val file = File(context.cacheDir, "temp_rescate_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream.use { it.copyTo(outputStream) }
            file
        } catch (e: Exception) { null }
    }
}
