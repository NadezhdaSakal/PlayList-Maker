package com.sakal.playlistmaker.settings.domain.impl

import com.sakal.playlistmaker.settings.domain.SettingsInteractor
import com.sakal.playlistmaker.settings.domain.SettingsRepository

class SettingsInteractorImpl(private val themeSwitchRepository: SettingsRepository) :
    SettingsInteractor {
    override fun switch(isDarkModeOn: Boolean) {
        themeSwitchRepository.switchTheme(isDarkModeOn)
    }

    override fun isDarkModeOn(): Boolean {
        return themeSwitchRepository.isDarkModeOn()
    }

    override fun applyCurrentTheme() {
        themeSwitchRepository.applyCurrentTheme()
    }
}