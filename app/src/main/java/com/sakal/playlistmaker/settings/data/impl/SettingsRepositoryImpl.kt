package com.sakal.playlistmaker.settings.data.impl

import androidx.appcompat.app.AppCompatDelegate
import com.sakal.playlistmaker.settings.data.LocalStorage
import com.sakal.playlistmaker.settings.domain.api.SettingsRepository

class SettingsRepositoryImpl(private val themeStorage: LocalStorage) : SettingsRepository {
    override fun switchTheme(darkThemeEnabled: Boolean) {
        themeStorage.switch(darkThemeEnabled)
        applyCurrentTheme()
    }

    override fun isDarkModeOn(): Boolean {
        return themeStorage.isDarkModeOn()
    }

    override fun applyCurrentTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeOn()) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}