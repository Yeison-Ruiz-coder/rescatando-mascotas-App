package com.example.rescatando_mascotas_forever.presentation.fundacion

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.data.repository.MascotaRepository
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

sealed class FormState {
    object Idle : FormState()
    object Loading : FormState()
    object Success : FormState()
    data class Error(val message: String) : FormState()
}

class FoundationMascotaFormViewModel(
    private val repository: MascotaRepository = MascotaRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<FormState>(FormState.Idle)
    val state: StateFlow<FormState> = _state.asStateFlow()

    private val _mascota = MutableStateFlow<Mascota?>(null)
    val mascota: StateFlow<Mascota?> = _mascota.asStateFlow()

    fun loadMascota(id: Int) {
        viewModelScope.launch {
            _state.value = FormState.Loading
            repository.getMascotaById(id).collect { result ->
                result.onSuccess {
                    _mascota.value = it
                    _state.value = FormState.Idle
                }.onFailure {
                    _state.value = FormState.Error("Error al cargar la mascota")
                }
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
        estado: String,
        ubicacion: String,
        descripcion: String,
        condiciones: String,
        salud: String,
        esterilizado: Boolean,
        desparasitado: Boolean,
        vacunado: Boolean,
        aptoNinos: Boolean,
        aptoAnimales: Boolean,
        hogarTemporal: Boolean,
        fotoPrincipalUri: Uri?,
        galeriaUris: List<Uri>
    ) {
        viewModelScope.launch {
            _state.value = FormState.Loading

            val partMap = mutableMapOf<String, RequestBody>()
            partMap["nombre_mascota"] = nombre.toRequestBody("text/plain".toMediaTypeOrNull())
            partMap["especie"] = especie.toRequestBody("text/plain".toMediaTypeOrNull())
            partMap["edad_aprox"] = edad.toRequestBody("text/plain".toMediaTypeOrNull())
            partMap["peso_aprox"] = peso.toRequestBody("text/plain".toMediaTypeOrNull())
            partMap["tamano"] = tamano.toRequestBody("text/plain".toMediaTypeOrNull())
            partMap["genero"] = genero.toRequestBody("text/plain".toMediaTypeOrNull())
            partMap["estado"] = estado.toRequestBody("text/plain".toMediaTypeOrNull())
            partMap["lugar_rescate"] = ubicacion.toRequestBody("text/plain".toMediaTypeOrNull())
            partMap["descripcion"] = descripcion.toRequestBody("text/plain".toMediaTypeOrNull())
            partMap["condiciones_especiales"] = condiciones.toRequestBody("text/plain".toMediaTypeOrNull())
            partMap["salud_general"] = salud.toRequestBody("text/plain".toMediaTypeOrNull())
            partMap["esterilizado"] = (if (esterilizado) "1" else "0").toRequestBody("text/plain".toMediaTypeOrNull())
            partMap["desparasitado"] = (if (desparasitado) "1" else "0").toRequestBody("text/plain".toMediaTypeOrNull())
            partMap["vacunado"] = (if (vacunado) "1" else "0").toRequestBody("text/plain".toMediaTypeOrNull())
            partMap["apto_con_ninos"] = (if (aptoNinos) "1" else "0").toRequestBody("text/plain".toMediaTypeOrNull())
            partMap["apto_con_otros_animales"] = (if (aptoAnimales) "1" else "0").toRequestBody("text/plain".toMediaTypeOrNull())
            partMap["necesita_hogar_temporal"] = (if (hogarTemporal) "1" else "0").toRequestBody("text/plain".toMediaTypeOrNull())

            if (id != null) {
                partMap["_method"] = "PUT".toRequestBody("text/plain".toMediaTypeOrNull())
            }

            val fotoPrincipalPart = fotoPrincipalUri?.let { uriToMultipart(context, it, "foto_principal") }
            val galeriaParts = galeriaUris.mapNotNull { uriToMultipart(context, it, "galeria_fotos[]") }

            val result = if (id == null) {
                repository.storeMascota(partMap, fotoPrincipalPart, galeriaParts)
            } else {
                repository.updateMascota(id, partMap, fotoPrincipalPart, galeriaParts)
            }

            result.onSuccess {
                _state.value = FormState.Success
            }.onFailure {
                _state.value = FormState.Error(it.message ?: "Error al guardar")
            }
        }
    }

    private fun uriToMultipart(context: Context, uri: Uri, name: String): MultipartBody.Part? {
        val file = uriToFile(context, uri) ?: return null
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, file.name, requestFile)
    }

    private fun uriToFile(context: Context, uri: Uri): File? {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return file
    }
}
