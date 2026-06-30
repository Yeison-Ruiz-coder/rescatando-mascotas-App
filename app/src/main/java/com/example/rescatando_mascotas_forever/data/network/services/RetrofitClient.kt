package com.example.rescatando_mascotas_forever.data.network.services

import com.example.rescatando_mascotas_forever.data.network.api.*
import com.example.rescatando_mascotas_forever.utils.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private var authToken: String? = null

    fun setToken(token: String?) {
        authToken = token
    }

    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .addHeader("Accept", "application/json")

        // No forzamos Content-Type aquí para que Retrofit pueda establecer
        // automáticamente el 'boundary' en peticiones Multipart.
        // Si la petición es JSON normal, Retrofit ya añade el Header vía Converter.

        authToken?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        chain.proceed(requestBuilder.build())
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    val mascotaApi: MascotaApi by lazy { retrofit.create(MascotaApi::class.java) }
    val eventoApi: EventoApi by lazy { retrofit.create(EventoApi::class.java) }
    val rescueApi: RescateApi by lazy { retrofit.create(RescateApi::class.java) }
    val adopcionApi: AdopcionApi by lazy { retrofit.create(AdopcionApi::class.java) }
    val veterinariaApi: VeterinariaApi by lazy { retrofit.create(VeterinariaApi::class.java) }
    val suscripcionApi: SuscripcionApi by lazy { retrofit.create(SuscripcionApi::class.java) }
}
