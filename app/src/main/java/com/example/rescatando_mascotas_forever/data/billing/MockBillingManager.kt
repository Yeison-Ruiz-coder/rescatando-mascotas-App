package com.example.rescatando_mascotas_forever.data.billing

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.rescatando_mascotas_forever.presentation.suscripciones.BillingState
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Simulador de BillingManager para pruebas en emuladores sin Google Play.
 */
class MockBillingManager(private val context: Context) {

    fun startConnection(
        onConnected: () -> Unit,
        onError: (String) -> Unit
    ) {
        // Simulamos un pequeño retraso de red
        Handler(Looper.getMainLooper()).postDelayed({
            onConnected()
        }, 1000)
    }

    fun queryProducts(onResult: (List<MockProduct>) -> Unit) {
        val mockProducts = listOf(
            MockProduct(
                id = "premium_subscription",
                title = "Plan Rescatista Premium",
                description = "Desbloquea historial médico, recordatorios y más.",
                price = "$19.900 COP / mes"
            ),
            MockProduct(
                id = "gold_subscription",
                title = "Plan Oro",
                description = "Todos los beneficios + insignias exclusivas.",
                price = "$35.000 COP / mes"
            )
        )
        onResult(mockProducts)
    }

    fun launchPurchase(activity: Activity, product: MockProduct, onSuccess: () -> Unit) {
        // Simulamos el proceso de compra
        Toast.makeText(context, "Iniciando compra simulada de: ${product.title}", Toast.LENGTH_SHORT).show()
        
        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(context, "¡Compra Exitosa! Ahora eres Premium", Toast.LENGTH_LONG).show()
            onSuccess()
        }, 2000)
    }
}

data class MockProduct(
    val id: String,
    val title: String,
    val description: String,
    val price: String
)
