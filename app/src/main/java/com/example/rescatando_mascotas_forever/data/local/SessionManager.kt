package com.example.rescatando_mascotas_forever.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.rescatando_mascotas_forever.data.network.models.User
import com.google.gson.Gson

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveSession(token: String, user: User) {
        val editor = prefs.edit()
        editor.putString("auth_token", token)
        editor.putString("user_data", gson.toJson(user))
        editor.putBoolean("is_logged_in", true)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString("auth_token", null)
    }

    fun getUser(): User? {
        val userJson = prefs.getString("user_data", null)
        return if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else null
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("is_logged_in", false)
    }

    fun logout() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}
