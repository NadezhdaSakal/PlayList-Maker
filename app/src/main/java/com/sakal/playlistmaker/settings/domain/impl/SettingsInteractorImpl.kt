package com.sakal.playlistmaker.settings.domain.impl

import com.sakal.playlistmaker.settings.domain.SettingsInteractor
import com.sakal.playlistmaker.settings.domain.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {
    override fun switch(isDarkModeOn: Boolean) {
        settingsRepository.switchTheme(isDarkModeOn)
    }

    override fun isDarkModeOn(): Boolean {
        return settingsRepository.isDarkModeOn()
    }

    override fun applyCurrentTheme() {
        settingsRepository.applyCurrentTheme()
    }
}