package com.udacity.project4.utils

import android.content.Context
import android.content.SharedPreferences

private const val USER = "USER"

private const val IS_LOGGED = "isLogged"

class LocationReminderPrefs(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences(USER, Context.MODE_PRIVATE)

    var isLogged: Boolean
        get() = preferences.getBoolean(IS_LOGGED, false)
        set(value) = preferences.edit()
            .putBoolean(IS_LOGGED, value)
            .apply()

}