package com.sakal.playlistmaker.settings.ui.view_model

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sakal.playlistmaker.App

class SettingsViewModel(application: App) : AndroidViewModel(application) {
    private val switchThemeInteractor = application.settingsInteractor
    fun switchTheme(isChecked: Boolean) {
        switchThemeInteractor.switch(isChecked)
    }

    fun isDarkThemeOn(): Boolean {
        return switchThemeInteractor.isDarkModeOn()
    }

    companion object {
        fun getViewModelFactory(application: App): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(
                        application = application
                    ) as T
                }
            }
    }
}