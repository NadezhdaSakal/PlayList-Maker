package com.sakal.playlistmaker.settings.data.impl

import androidx.appcompat.app.AppCompatDelegate
import com.sakal.playlistmaker.settings.data.preferences.ThemeStorage
import com.sakal.playlistmaker.settings.domain.SettingsRepository

class SettingsRepoImpl(private val themeStorage: ThemeStorage) : SettingsRepository {

    override fun switchTheme(darkThemeEnabled: Boolean) {
        themeStorage.switch(darkThemeEnabled)
        applyCurrentTheme()
    }

    override fun isDarkMode(): Boolean {
        return themeStorage.isDarkMode()
    }

    override fun applyCurrentTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode()) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}