package com.gdbm.dangoapp.utils

import android.content.Context

object Preferences {

    fun setLastUpdate(value:String, context: Context) {
        val sharedPref= context.getSharedPreferences(
            SHARED_PREFERENCES_NAME + Configs.CURRENT_LANGUAGE, Context.MODE_PRIVATE)
        with (sharedPref?.edit()) {
            this?.putString(EXPIRATION_KEY, value)
            this?.commit()
        }
    }

    fun getLastUpdate(context: Context): String? {
        return context.getSharedPreferences(
            SHARED_PREFERENCES_NAME + Configs.CURRENT_LANGUAGE,
            Context.MODE_PRIVATE)?.getString(EXPIRATION_KEY,null)
    }

    private const val SHARED_PREFERENCES_NAME = "DangoSharedPreferences"
    private const val EXPIRATION_KEY = "ExpirationKey"
}