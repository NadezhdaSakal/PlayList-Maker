package com.sakal.playlistmaker.settings.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.sakal.playlistmaker.settings.domain.SettingsInteractor


class SettingsViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {

    fun switchTheme(isChecked: Boolean) {
        settingsInteractor.switch(isChecked)
    }

    fun isDarkTheme(): Boolean {
        return settingsInteractor.isDarkMode()
    }


}