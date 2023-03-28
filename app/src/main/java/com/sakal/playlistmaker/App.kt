package com.sakal.playlistmaker

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate


class App : Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val preferences = getSharedPreferences(Constants.PLAYLIST_MAKER_PREFS, MODE_PRIVATE)
        darkTheme = preferences.getBoolean(Constants.DARK_THEME_KEY, false)

        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme || isDarkMode(applicationContext as App)) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun isDarkMode(context: Context): Boolean {
        val darkModeFlag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
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

    fun saveTheme(darkThemeEnabled: Boolean){
        getSharedPreferences(Constants.PLAYLIST_MAKER_PREFS, MODE_PRIVATE)
            .edit()
            .putBoolean(Constants.DARK_THEME_KEY, darkThemeEnabled)
            .apply()
    }
}
