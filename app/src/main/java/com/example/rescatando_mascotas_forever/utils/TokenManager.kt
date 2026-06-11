/*package com.example.rescatando_mascotas_forever.utils

import android.content.Context

class TokenManager(context: Context) {
}*/

package com.example.rescatando_mascotas_forever.utils

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val USER_TOKEN = "user_token"
    }

    fun saveToken(token: String) {
        sharedPreferences.edit()
            .putString(USER_TOKEN, token)
            .apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(USER_TOKEN, null)
    }

    fun clearToken() {
        sharedPreferences.edit()
            .remove(USER_TOKEN)
            .apply()
    }
}