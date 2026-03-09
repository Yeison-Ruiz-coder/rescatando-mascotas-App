package com.example.rescatando_mascotas_forever

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.rescatando_mascotas_forever.presentation.common.navigation.AppNavigation
import com.example.rescatando_mascotas_forever.ui.theme.RescatandomascotasforeverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RescatandomascotasforeverTheme {
                AppNavigation()
            }
        }
    }
}
