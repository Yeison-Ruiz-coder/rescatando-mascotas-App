package com.example.rescatando_mascotas_forever.data.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*

class BillingManager(
    private val context: Context
) : PurchasesUpdatedListener {

    private val billingClient = BillingClient.newBuilder(context)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .build()
        )
        .setListener(this)
        .build()

    fun startConnection(
        onConnected: () -> Unit,
        onError: (String) -> Unit
    ) {
        billingClient.startConnection(
            object : BillingClientStateListener {

                override fun onBillingSetupFinished(
                    billingResult: BillingResult
                ) {
                    if (
                        billingResult.responseCode ==
                        BillingClient.BillingResponseCode.OK
                    ) {
                        onConnected()
                    } else {
                        onError("Error de conexión a Google Play: ${billingResult.debugMessage}")
                    }
                }

                override fun onBillingServiceDisconnected() {
                    onError("Se perdió la conexión con Google Play")
                }
            }
        )
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {

        if (
            billingResult.responseCode ==
            BillingClient.BillingResponseCode.OK
            && purchases != null
        ) {

            purchases.forEach { purchase ->
                handlePurchase(purchase)
            }

        }
    }

    private fun handlePurchase(
        purchase: Purchase
    ) {

        if (
            purchase.purchaseState ==
            Purchase.PurchaseState.PURCHASED
        ) {

            if (!purchase.isAcknowledged) {

                val acknowledgeParams =
                    AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(
                            purchase.purchaseToken
                        )
                        .build()

                billingClient.acknowledgePurchase(
                    acknowledgeParams
                ) {
                }
            }
        }
    }

    fun queryProducts(
        onResult: (List<ProductDetails>) -> Unit
    ) {

        val productList = listOf(

            QueryProductDetailsParams.Product
                .newBuilder()
                .setProductId(
                    "premium_subscription"
                )
                .setProductType(
                    BillingClient.ProductType.SUBS
                )
                .build()
        )

        val params =
            QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build()

        billingClient.queryProductDetailsAsync(
            params
        ) { _, productDetailsList ->

            onResult(productDetailsList)
        }
    }

    fun launchPurchase(
        activity: Activity,
        productDetails: ProductDetails
    ) {

        val offerToken =
            productDetails.subscriptionOfferDetails
                ?.firstOrNull()
                ?.offerToken
                ?: return

        val productDetailsParams =
            BillingFlowParams.ProductDetailsParams
                .newBuilder()
                .setProductDetails(productDetails)
                .setOfferToken(offerToken)
                .build()

        val billingFlowParams =
            BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(
                    listOf(productDetailsParams)
                )
                .build()

        billingClient.launchBillingFlow(
            activity,
            billingFlowParams
        )
    }
}