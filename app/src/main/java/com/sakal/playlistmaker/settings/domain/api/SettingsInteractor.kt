package com.sakal.playlistmaker.settings.domain.api

interface SettingsInteractor {
    fun switch(isDarkModeOn: Boolean)
    fun isDarkModeOn(): Boolean
    fun applyCurrentTheme()
}