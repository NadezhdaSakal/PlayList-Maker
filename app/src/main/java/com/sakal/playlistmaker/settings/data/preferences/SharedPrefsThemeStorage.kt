package com.sakal.playlistmaker.settings.data.preferences

import android.content.SharedPreferences
import com.sakal.playlistmaker.Constants

class SharedPrefsThemeStorage(private val preferences: SharedPreferences): ThemeStorage {

    override fun switch(darkThemeEnabled: Boolean) {
        preferences
            .edit()
            .putBoolean(Constants.DARK_THEME_KEY, darkThemeEnabled)
            .apply()
    }

    override fun isDarkModeOn(): Boolean {
        return preferences.getBoolean(Constants.DARK_THEME_KEY, false)
    }

}