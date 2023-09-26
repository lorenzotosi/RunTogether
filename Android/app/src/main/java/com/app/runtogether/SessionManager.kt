package com.app.runtogether

import android.content.Context
import android.content.SharedPreferences
import com.app.runtogether.db.user.User


class SessionManager(private val _context: Context) {
    // Shared Preferences

    private val pref: SharedPreferences = _context.getSharedPreferences(PREF_NAME, 0)
    private val editor: SharedPreferences.Editor = pref.edit()
    companion object {
        private const val PREF_NAME = "AndroidHivePref"
        private const val IS_LOGIN = "IsLoggedIn"
        const val KEY_NAME = "name"
        const val KEY_EMAIL = "email"
        const val KEY_PASSWORD = "password"

        fun createLoginSession(context: Context, username: String, email: String, password: String) {
            val pref = context.getSharedPreferences(PREF_NAME, 0)
            val editor = pref.edit()
            editor.putBoolean(IS_LOGIN, true)
            editor.putString(KEY_NAME, username)
            editor.putString(KEY_EMAIL, email)
            editor.putString(KEY_PASSWORD, password)
            editor.apply()
        }

        fun getUserDetails(context: Context): User{
            return User(
                username = context.getSharedPreferences(PREF_NAME, 0).getString(KEY_NAME, "").toString(),
                email = context.getSharedPreferences(PREF_NAME, 0).getString(KEY_EMAIL, "").toString(),
                password = context.getSharedPreferences(PREF_NAME, 0).getString(KEY_PASSWORD, "").toString()
            )
        }

        fun isLoggedIn(context: Context): Boolean {
            val pref = context.getSharedPreferences(PREF_NAME, 0)
            return pref.getBoolean(IS_LOGIN, false)
        }

        fun logoutUser(context: Context) {
            val pref = context.getSharedPreferences(PREF_NAME, 0)
            val editor = pref.edit()
            editor.clear()
            editor.apply()
        }



    }
}