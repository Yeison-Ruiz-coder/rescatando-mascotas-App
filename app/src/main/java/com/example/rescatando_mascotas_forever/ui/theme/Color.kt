package com.example.rescatando_mascotas_forever.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * COLORES SÓLIDOS (Estáticos para asegurar contraste)
 * Útiles para componentes que no deben cambiar con el tema (ej. botones sobre fondos oscuros)
 */
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Transparent = Color(0x00000000)

// Colores de Contraste Fijo
val StaticWhite = Color(0xFFFFFFFF)
val StaticBlack = Color(0xFF1B202B)
val StaticDarkCard = Color(0xFF1E293B)
val StaticLightCard = Color(0xFFFFFFFF)

/**
 * PALETA WEB - MODO CLARO (Preparada para estilos globales)
 */
val WebPrimary = Color(0xFF667EEA)
val WebSecondary = Color(0xFF764BA2)
val WebPrimaryLight = Color(0xFF8B9CFF)
val WebSecondaryLight = Color(0xFF9B6BC4)

val WebAccent = Color(0xFFFF8C42)
val WebHeart = Color(0xFFFF6B9D)
val WebSuccess = Color(0xFF2ECC71)
val WebDanger = Color(0xFFFF4757)
val WebWarning = Color(0xFFFFC107)
val WebInfo = Color(0xFF4299E1)

val WebLight = Color(0xFFF7F2FC)
val WebBackgroundSection = Color(0xFFF4ECFC)
val WebLightSecondary = Color(0xFFF6E6FF)
val WebCardBg = Color(0xFFFFFFFF)
val WebBorder = Color(0x14000000)

val WebText = Color(0xFF1B202B)
val WebTextMuted = Color(0xFF39414D)
val WebTextLight = Color(0xFF7A8592)

// Fundaciones
val WebFundacionPrimary = Color(0xFFFF4757)
val WebFundacionSecondary = Color(0xFFFF6B81)

/**
 * PALETA WEB - MODO OSCURO
 */
val WebPrimaryDark = Color(0xFF8B9CFF)
val WebSecondaryDark = Color(0xFFB07CD4)
val WebPrimaryLightDark = Color(0xFFA0B0FF)
val WebSecondaryLightDark = Color(0xFFC49AE0)

val WebAccentDark = Color(0xFFFF9F5E)
val WebHeartDark = Color(0xFFFF85B3)
val WebSuccessDark = Color(0xFF3EE681)
val WebDangerDark = Color(0xFFFF6B7A)
val WebWarningDark = Color(0xFFFBBF24)
val WebInfoDark = Color(0xFF60A5FA)

val WebLightDark = Color(0xFF1A1A2E)
val WebBackgroundDark = Color(0xFF0F172A)
val WebBackgroundSectionDark = Color(0xFF070111)
val WebCardBgDark = Color(0xFF1E293B)
val WebBorderDark = Color(0x268B9CFF)

val WebTextDark = Color(0xFFF6F9FC)
val WebTextMutedDark = Color(0xFFD2DAE6)
val WebTextLightDark = Color(0xFFBBC9DD)

/**
 * ALIAS Y GRADIENTES
 */
val WebYellow = WebWarning
val WebVerifiedGreen = WebSuccess

val WebPurpleGradient = Brush.verticalGradient(
    colors = listOf(WebPrimary, WebSecondary)
)

val FoundationGradient = Brush.verticalGradient(
    colors = listOf(WebFundacionPrimary, WebFundacionSecondary)
)

/**
 * COMPATIBILIDAD (BRAND ANTERIOR)
 */
val BrandPurple = Color(0xFF673AB7)
val BrandPurpleLight = Color(0xFF9575CD)
val BrandPurpleDark = Color(0xFF4C35A3)
