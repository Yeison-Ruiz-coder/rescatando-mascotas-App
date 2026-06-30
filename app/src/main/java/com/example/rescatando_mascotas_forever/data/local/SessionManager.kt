package com.example.rescatando_mascotas_forever.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.rescatando_mascotas_forever.data.network.models.User
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private val _userFlow = MutableStateFlow<User?>(null)
        val userFlow: StateFlow<User?> = _userFlow.asStateFlow()
    }

    init {
        // Inicializar el flow con el usuario guardado si existe
        if (_userFlow.value == null) {
            _userFlow.value = getUser()
        }
    }

    fun saveSession(token: String, user: User) {
        val editor = prefs.edit()
        editor.putString("auth_token", token)
        editor.putString("user_data", gson.toJson(user))
        editor.putBoolean("is_logged_in", true)
        editor.apply()
        _userFlow.value = user
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

    fun updateUser(user: User) {
        val editor = prefs.edit()
        editor.putString("user_data", gson.toJson(user))
        editor.apply()
        _userFlow.value = user
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("is_logged_in", false)
    }

    fun logout() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
        _userFlow.value = null
    }
}
