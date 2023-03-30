package com.sakal.playlistmaker

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate


class App : Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(Constants.PLAYLIST_MAKER_PREFS, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(Constants.DARK_THEME_KEY, isDarkTheme(this))
        switchTheme(darkTheme)
    }

    private fun isDarkTheme(context: Context): Boolean {
        val isDarkMode: Boolean
        val currentNightMode =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        isDarkMode = when ((currentNightMode)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
        return isDarkMode
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun saveTheme(darkThemeEnabled: Boolean) {
        getSharedPreferences(Constants.PLAYLIST_MAKER_PREFS, MODE_PRIVATE)
            .edit()
            .putBoolean(Constants.DARK_THEME_KEY, darkThemeEnabled)
            .apply()
    }
}
