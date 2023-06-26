package com.sakal.playlistmaker.settings.domain.impl

import com.sakal.playlistmaker.settings.domain.SettingsInteractor
import com.sakal.playlistmaker.settings.domain.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {
    override fun switch(isDarkMode: Boolean) {
        settingsRepository.switchTheme(isDarkMode)
    }

    override fun isDarkMode(): Boolean {
        return settingsRepository.isDarkMode()
    }

    override fun applyCurrentTheme() {
        settingsRepository.applyCurrentTheme()
    }
}