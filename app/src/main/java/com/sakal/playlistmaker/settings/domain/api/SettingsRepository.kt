package com.sakal.playlistmaker.settings.domain.api


interface SettingsRepository {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun isDarkModeOn(): Boolean
    fun applyCurrentTheme()
}