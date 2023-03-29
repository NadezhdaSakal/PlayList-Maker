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
        darkTheme = preferences.getBoolean(Constants.DARK_THEME_KEY, isDarkMode(this))
        switchTheme(darkTheme)


    }

    private fun isDarkMode(context: Context): Boolean {
        var darkModeFlag: Boolean
         val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        darkModeFlag = when ((currentNightMode)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
        return darkModeFlag
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
