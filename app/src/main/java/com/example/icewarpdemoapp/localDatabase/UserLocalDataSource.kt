package com.example.icewarpdemoapp.localDatabase
import android.content.Context
import android.content.SharedPreferences

interface UserLocalDataSource {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
}

class UserLocalDataSourceImpl(
    private val context: Context
) : UserLocalDataSource {

    private val prefs =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    override fun saveToken(token: String) {
        prefs.edit()
            .putString("token", token)
            .apply()
    }

    override fun getToken(): String? {
        return prefs.getString("token", null)
    }

    override fun clearToken() {
        prefs.edit()
            .remove("token")
            .apply()
    }
}
