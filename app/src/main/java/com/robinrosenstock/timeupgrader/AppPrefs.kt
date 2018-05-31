package com.robinrosenstock.timeupgrader

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import net.danlew.android.joda.JodaTimeAndroid


val prefs: Prefs by lazy {
    AppPrefs.prefs!!
}


class AppPrefs : Application() {
    companion object {
        var prefs: Prefs? = null
    }

        override fun onCreate() {
        prefs = Prefs(applicationContext)
            JodaTimeAndroid.init(this)
        super.onCreate()
    }
}


class Prefs (context: Context) {
    val PREFS_FILENAME = "com.robinrosenstock.timeupgrader.prefs"

    val BACKGROUND_COLOR = "background_color"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    var bgColor: Int
        get() = prefs.getInt(BACKGROUND_COLOR, Color.BLACK)
        set(value) = prefs.edit().putInt(BACKGROUND_COLOR, value).apply()
}