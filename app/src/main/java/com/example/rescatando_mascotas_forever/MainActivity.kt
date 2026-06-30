package com.example.rescatando_mascotas_forever

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.rescatando_mascotas_forever.presentation.common.navigation.AppNavigation
import com.example.rescatando_mascotas_forever.ui.theme.RescatandomascotasforeverTheme
import com.example.rescatando_mascotas_forever.ui.theme.ThemeController
import com.example.rescatando_mascotas_forever.data.local.ThemeManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar el tema guardado
        val themeManager = ThemeManager(this)
        ThemeController.isDarkOverride.value = themeManager.getDarkMode()

        setContent {
            RescatandomascotasforeverTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}
