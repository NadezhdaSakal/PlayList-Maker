package com.sakal.playlistmaker

import android.app.Application
import com.sakal.playlistmaker.creator.Creator
import com.sakal.playlistmaker.settings.domain.SettingsInteractor


class App : Application() {
    lateinit var themeSwitcherInteractor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()
        themeSwitcherInteractor = Creator.provideThemeSwitchInteractor(this)
        themeSwitcherInteractor.applyCurrentTheme()
    }
}



