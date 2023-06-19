package com.sakal.playlistmaker.settings.ui.view_model

import androidx.lifecycle.ViewModel
import com.sakal.playlistmaker.settings.domain.SettingsInteractor


class SettingsViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {

    fun switchTheme(isChecked: Boolean) {
        settingsInteractor.switch(isChecked)
    }

    fun isDarkThemeOn(): Boolean {
        return settingsInteractor.isDarkModeOn()
    }


}