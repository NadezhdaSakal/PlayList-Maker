package com.sakal.playlistmaker.settings.data.preferences

import android.content.SharedPreferences
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.settings.data.LocalStorage

class ThemeStorage(private val sharedPreferences: SharedPreferences) : LocalStorage {

    override fun switch(darkThemeEnabled: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(Constants.DARK_THEME_KEY, darkThemeEnabled)
            .apply()
    }

    override fun isDarkModeOn(): Boolean {
        return sharedPreferences.getBoolean(Constants.DARK_THEME_KEY, false)
    }


}