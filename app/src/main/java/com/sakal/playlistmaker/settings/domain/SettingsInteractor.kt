package com.sakal.playlistmaker.settings.domain

interface SettingsInteractor {
    fun switch(isDarkMode: Boolean)
    fun isDarkMode(): Boolean
    fun applyCurrentTheme()
}