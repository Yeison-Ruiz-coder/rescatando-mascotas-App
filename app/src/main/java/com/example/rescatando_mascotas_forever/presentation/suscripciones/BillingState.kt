package com.example.rescatando_mascotas_forever.presentation.suscripciones

import com.example.rescatando_mascotas_forever.data.billing.MockProduct

sealed class BillingState {
    object Loading : BillingState()
    data class Success(val products: List<MockProduct>) : BillingState()
    data class Error(val message: String) : BillingState()
}
