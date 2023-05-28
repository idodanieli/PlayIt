package com.idodanieli.playit

import android.content.Context
import android.preference.PreferenceManager

class SharedPrefsManager(context: Context) {
    companion object {
        private const val SHARED_PREF_USERNAME = "USERNAME"
        const val USERNAME_DEFAULT_VALUE = "DEFAULT"

        @Volatile
        private var instance: SharedPrefsManager? = null

        fun initialize(context: Context) {
            if (instance == null) {
                instance = SharedPrefsManager(context)
            }
        }

        fun getInstance(): SharedPrefsManager {
            return instance!!
        }
    }

    private val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getUsername(): String {
        return mSharedPreferences.getString(SHARED_PREF_USERNAME, USERNAME_DEFAULT_VALUE)!!
    }

    fun setUsername(username: String) {
        val editor = mSharedPreferences.edit()
        editor.putString(SHARED_PREF_USERNAME, username)
        editor.commit()
    }
}