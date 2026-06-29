package com.example.rescatando_mascotas_forever.presentation.admin

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

sealed class AdminFormState {
    object Idle : AdminFormState()
    object Loading : AdminFormState()
    object Success : AdminFormState()
    data class Error(val message: String) : AdminFormState()
}

class AdminMascotaFormViewModel : ViewModel() {

    private val _state = MutableStateFlow<AdminFormState>(AdminFormState.Idle)
    val state: StateFlow<AdminFormState> = _state.asStateFlow()

    private val _mascota = MutableStateFlow<Mascota?>(null)
    val mascota: StateFlow<Mascota?> = _mascota.asStateFlow()

    fun loadMascota(id: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.mascotaApi.getMascotaById(id)
                if (response.success && response.data != null) {
                    val gson = com.google.gson.Gson()
                    val json = gson.toJson(response.data)
                    val mascotaObj = gson.fromJson(json, Mascota::class.java)
                    _mascota.value = mascotaObj
                }
            } catch (e: Exception) {
                Log.e("AdminMascotaFormVM", "Error cargando mascota: ${e.message}")
            }
        }
    }

    fun saveMascota(
        context: Context,
        id: Int?,
        nombre: String,
        especie: String,
        edad: String,
        peso: String,
        tamano: String,
        genero: String,
        color: String,
        estado: String,
        ubicacion: String,
        descripcion: String,
        condiciones: String,
        salud: String,
        enfermedades: String,
        medicamentos: String,
        esterilizado: Boolean,
        desparasitado: Boolean,
        vacunado: Boolean,
        aptoNinos: Boolean,
        aptoAnimales: Boolean,
        hogarTemporal: Boolean,
        destacada: Boolean,
        requisitos: String,
        hogarRecomendado: String,
        videoUrl: String,
        fotoPrincipalUri: Uri?,
        fundacionId: String
    ) {
        viewModelScope.launch {
            _state.value = AdminFormState.Loading
            try {
                val partMap = mutableMapOf<String, RequestBody>()
                partMap["nombre_mascota"] = nombre.toRequestBody("text/plain".toMediaTypeOrNull())
                partMap["especie"] = especie.toRequestBody("text/plain".toMediaTypeOrNull())
                
                if (edad.isNotBlank()) partMap["edad_aprox"] = edad.toRequestBody("text/plain".toMediaTypeOrNull())
                if (peso.isNotBlank()) partMap["peso_aprox"] = peso.toRequestBody("text/plain".toMediaTypeOrNull())
                
                partMap["tamano"] = tamano.toRequestBody("text/plain".toMediaTypeOrNull())
                partMap["genero"] = genero.toRequestBody("text/plain".toMediaTypeOrNull())
                partMap["color"] = color.toRequestBody("text/plain".toMediaTypeOrNull())
                partMap["estado"] = estado.toRequestBody("text/plain".toMediaTypeOrNull())
                partMap["lugar_rescate"] = ubicacion.toRequestBody("text/plain".toMediaTypeOrNull())
                partMap["descripcion"] = descripcion.toRequestBody("text/plain".toMediaTypeOrNull())
                partMap["condiciones_especiales"] = condiciones.toRequestBody("text/plain".toMediaTypeOrNull())
                partMap["salud_general"] = salud.toRequestBody("text/plain".toMediaTypeOrNull())
                partMap["hogar_recomendado"] = hogarRecomendado.toRequestBody("text/plain".toMediaTypeOrNull())
                partMap["video_url"] = videoUrl.toRequestBody("text/plain".toMediaTypeOrNull())
                partMap["fundacion_id"] = fundacionId.toRequestBody("text/plain".toMediaTypeOrNull())
                
                // Booleanos como 1 o 0
                partMap["esterilizado"] = (if (esterilizado) "1" else "0").toRequestBody("text/plain".toMediaTypeOrNull())
                partMap["desparasitado"] = (if (desparasitado) "1" else "0").toRequestBody("text/plain".toMediaTypeOrNull())
                partMap["vacunado"] = (if (vacunado) "1" else "0").toRequestBody("text/plain".toMediaTypeOrNull())
                partMap["apto_con_ninos"] = (if (aptoNinos) "1" else "0").toRequestBody("text/plain".toMediaTypeOrNull())
                partMap["apto_con_otros_animales"] = (if (aptoAnimales) "1" else "0").toRequestBody("text/plain".toMediaTypeOrNull())
                partMap["necesita_hogar_temporal"] = (if (hogarTemporal) "1" else "0").toRequestBody("text/plain".toMediaTypeOrNull())
                partMap["destacada"] = (if (destacada) "1" else "0").toRequestBody("text/plain".toMediaTypeOrNull())

                // Convertir strings con comas a arreglos para Laravel
                fun String.toPartList(key: String) {
                    this.split(",").map { it.trim() }.filter { it.isNotBlank() }.forEachIndexed { index, s ->
                        partMap["$key[$index]"] = s.toRequestBody("text/plain".toMediaTypeOrNull())
                    }
                }

                enfermedades.toPartList("enfermedades_cronicas")
                medicamentos.toPartList("medicamentos")
                requisitos.toPartList("requisitos_adopcion")

                var imagePart: MultipartBody.Part? = null
                fotoPrincipalUri?.let { uri ->
                    try {
                        val file = uriToFile(context, uri)
                        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                        imagePart = MultipartBody.Part.createFormData("foto_principal", file.name, requestFile)
                    } catch (e: Exception) {
                        Log.e("AdminMascotaFormVM", "Error procesando imagen: ${e.message}")
                    }
                }

                val response = if (id == null) {
                    RetrofitClient.mascotaApi.adminStoreMascota(partMap, imagePart)
                } else {
                    partMap["_method"] = "PUT".toRequestBody("text/plain".toMediaTypeOrNull())
                    RetrofitClient.mascotaApi.adminUpdateMascota(id, partMap, imagePart)
                }

                if (response.success) {
                    _state.value = AdminFormState.Success
                } else {
                    _state.value = AdminFormState.Error(response.message ?: "Error al guardar")
                }
            } catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("AdminMascotaFormVM", "Error 422 detalles: $errorBody")
                _state.value = AdminFormState.Error("Error de validación: Verifique los campos")
            } catch (e: Exception) {
                Log.e("AdminMascotaFormVM", "Error al guardar: ${e.message}", e)
                _state.value = AdminFormState.Error("Error: ${e.localizedMessage}")
            }
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File {
        val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        return file
    }
}
