package com.sakal.playlistmaker.settings.data.preferences

interface ThemeStorage {
    fun switch(darkThemeEnabled: Boolean)
    fun isDarkMode(): Boolean
}