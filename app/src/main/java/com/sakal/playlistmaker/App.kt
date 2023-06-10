package com.sakal.playlistmaker

import android.app.Application
import com.sakal.playlistmaker.creator.Creator
import com.sakal.playlistmaker.settings.domain.api.SettingsInteractor


class App : Application() {
    lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()

        settingsInteractor = Creator.provideSettingsInteractor(this)
        settingsInteractor.applyCurrentTheme()
    }
}



