package com.sakal.playlistmaker

import android.app.Application
import com.sakal.playlistmaker.search.di.Creator
import com.sakal.playlistmaker.settings.domain.SettingsInteractor


class App : Application() {
    lateinit var themeSwitcherInteractor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()
        themeSwitcherInteractor = Creator.provideThemeSwitchInteractor(this)
        themeSwitcherInteractor.applyCurrentTheme()
    }
}



