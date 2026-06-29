package com.example.rescatando_mascotas_forever.data.local

import android.content.Context
import android.content.SharedPreferences

class ThemeManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    fun setDarkMode(isDark: Boolean?) {
        val editor = prefs.edit()
        if (isDark == null) {
            editor.remove("is_dark_mode")
        } else {
            editor.putBoolean("is_dark_mode", isDark)
        }
        editor.apply()
    }

    fun getDarkMode(): Boolean? {
        if (!prefs.contains("is_dark_mode")) return null
        return prefs.getBoolean("is_dark_mode", false)
    }
}
