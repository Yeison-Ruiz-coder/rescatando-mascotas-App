package com.example.rescatando_mascotas_forever.presentation.fundacion

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.data.network.services.RetrofitClient
import com.example.rescatando_mascotas_forever.data.repository.FoundationMascotasRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

sealed class FormState {
    object Idle : FormState()
    object Loading : FormState()
    object Success : FormState()
    data class Error(val message: String) : FormState()
}

data class FormData(
    val razas: List<RazaItem> = emptyList(),
    val vacunas: List<VacunaItem> = emptyList()
)

data class RazaItem(val id: Int, val nombre_raza: String, val especie: String)
data class VacunaItem(val id: Int, val nombre: String)

class FoundationMascotaFormViewModel(
    private val repository: FoundationMascotasRepository = FoundationMascotasRepository(RetrofitClient.entityApi)
) : ViewModel() {

    private val _state = MutableStateFlow<FormState>(FormState.Idle)
    val state: StateFlow<FormState> = _state.asStateFlow()

    private val _mascota = MutableStateFlow<Mascota?>(null)
    val mascota: StateFlow<Mascota?> = _mascota.asStateFlow()

    private val _formData = MutableStateFlow(FormData())
    val formData: StateFlow<FormData> = _formData.asStateFlow()

    init {
        loadFormData()
    }

    private fun loadFormData() {
        viewModelScope.launch {
            try {
                val response = repository.getMascotasFormData()
                if (response.success && response.data != null) {
                    val gson = Gson()
                    val razasData = response.data["razas"]
                    val vacunasData = response.data["vacunas"]
                    
                    val razas: List<RazaItem> = if (razasData != null) {
                        gson.fromJson(gson.toJson(razasData), object : TypeToken<List<RazaItem>>() {}.type)
                    } else emptyList()
                    
                    val vacunas: List<VacunaItem> = if (vacunasData != null) {
                        gson.fromJson(gson.toJson(vacunasData), object : TypeToken<List<VacunaItem>>() {}.type)
                    } else emptyList()
                    
                    _formData.value = FormData(razas, vacunas)
                }
            } catch (e: Exception) { 
                _formData.value = FormData(emptyList(), emptyList())
            }
        }
    }

    fun loadMascota(id: Int) {
        viewModelScope.launch {
            _state.value = FormState.Loading
            try {
                val response = repository.getMascotaDetalle(id)
                if (response.success && response.data != null) {
                    _mascota.value = response.data
                    _state.value = FormState.Idle
                }
            } catch (e: Exception) {
                _state.value = FormState.Error("Error al cargar detalle")
            }
        }
    }

    fun saveMascota(
        context: Context,
        mascotaId: Int?,
        rescateId: Int?,
        fields: Map<String, String>,
        arrays: Map<String, List<String>>,
        fotoPrincipalUri: Uri?,
        galeriaUris: List<Uri>
    ) {
        viewModelScope.launch {
            _state.value = FormState.Loading
            try {
                val fotoPrincipalPart = fotoPrincipalUri?.let { uriToMultipart(context, it, "foto_principal") }
                val galeriaParts = galeriaUris.mapNotNull { uriToMultipart(context, it, "galeria_fotos[]") }

                val response = repository.saveMascota(mascotaId, fields, arrays, fotoPrincipalPart, galeriaParts, rescateId)
                if (response.success) {
                    _state.value = FormState.Success
                } else {
                    _state.value = FormState.Error(response.message ?: "Error al guardar")
                }
            } catch (e: Exception) {
                _state.value = FormState.Error(e.message ?: "Error de conexión")
            }
        }
    }

    fun deleteMascota(id: Int) {
        viewModelScope.launch {
            _state.value = FormState.Loading
            try {
                val response = repository.eliminarMascota(id)
                if (response.success) {
                    _state.value = FormState.Success
                } else {
                    _state.value = FormState.Error(response.message ?: "Error al eliminar")
                }
            } catch (e: Exception) {
                _state.value = FormState.Error("Error de red: ${e.message}")
            }
        }
    }

    private fun uriToMultipart(context: Context, uri: Uri, name: String): MultipartBody.Part? {
        val file = uriToFile(context, uri) ?: return null
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, file.name, requestFile)
    }

    private fun uriToFile(context: Context, uri: Uri): File? {
        try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val file = File(context.cacheDir, "temp_pet_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream.use { input -> outputStream.use { output -> input.copyTo(output) } }
            return file
        } catch (e: Exception) { return null }
    }
}
