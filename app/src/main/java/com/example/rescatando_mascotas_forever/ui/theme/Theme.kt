package com.example.rescatando_mascotas_forever.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Objeto global para controlar el tema manualmente
object ThemeController {
    // null = Seguir al sistema, true = Forzar oscuro, false = Forzar claro
    var isDarkOverride = mutableStateOf<Boolean?>(null)
}

/**
 * PALETA WEB - MODO OSCURO
 */
private val WebDarkColorScheme = darkColorScheme(
    primary = WebPrimaryDark,
    secondary = WebSecondaryDark,
    tertiary = WebAccentDark,
    background = WebBackgroundSectionDark,
    surface = WebCardBgDark,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onBackground = WebTextDark,
    onSurface = WebTextDark,
    surfaceVariant = WebLightDark,
    outline = WebBorderDark,
    error = WebDangerDark,
    onError = White
)

/**
 * PALETA WEB - MODO CLARO
 */
private val WebLightColorScheme = lightColorScheme(
    primary = WebPrimary,
    secondary = WebSecondary,
    tertiary = WebAccent,
    background = WebLight,
    surface = WebCardBg,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onBackground = WebText,
    onSurface = WebText,
    surfaceVariant = WebBackgroundSection,
    outline = WebBorder,
    error = WebDanger,
    onError = White
)

@Composable
fun RescatandomascotasforeverTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Lógica preparada para el override manual
    val useDarkTheme = ThemeController.isDarkOverride.value ?: darkTheme

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        useDarkTheme -> WebDarkColorScheme
        else -> WebLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
