package com.sakal.playlistmaker

import android.app.Application
import com.sakal.playlistmaker.di.dataModule
import com.sakal.playlistmaker.di.interactorModule
import com.sakal.playlistmaker.di.repositoryModule
import com.sakal.playlistmaker.di.viewModelModule
import com.sakal.playlistmaker.settings.domain.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                listOf(
                    dataModule,
                    repositoryModule,
                    interactorModule,
                    viewModelModule
                )
            )
        }
        val settingsInteractor: SettingsInteractor by inject()
        settingsInteractor.isDarkMode()
    }
}
