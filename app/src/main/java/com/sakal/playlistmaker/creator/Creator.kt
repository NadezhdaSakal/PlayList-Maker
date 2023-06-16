package com.sakal.playlistmaker.creator

import android.content.Context
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.player.data.impl.PlayerRepoImpl
import com.sakal.playlistmaker.player.domain.PlayerInteractor
import com.sakal.playlistmaker.player.domain.PlayerRepo
import com.sakal.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.sakal.playlistmaker.search.data.impl.TracksRepoImpl
import com.sakal.playlistmaker.search.data.network.RetrofitClient
import com.sakal.playlistmaker.search.data.storage.LocalStorage
import com.sakal.playlistmaker.search.domain.TracksInteractor
import com.sakal.playlistmaker.search.domain.TracksRepo
import com.sakal.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.sakal.playlistmaker.settings.data.impl.SettingsRepoImpl
import com.sakal.playlistmaker.settings.data.preferences.ThemeStorage
import com.sakal.playlistmaker.settings.domain.SettingsInteractor
import com.sakal.playlistmaker.settings.domain.SettingsRepository
import com.sakal.playlistmaker.settings.domain.impl.SettingsInteractorImpl

object Creator {

    private fun getTracksRepository(context: Context): TracksRepo {
        return TracksRepoImpl(
            RetrofitClient(context),
            LocalStorage(context.getSharedPreferences(Constants.HISTORY_TRACKS_SHARED_PREF, Context.MODE_PRIVATE))
        )
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }

    private fun getPlayerRepo(): PlayerRepo {
        return PlayerRepoImpl()
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepo())
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepoImpl (
            ThemeStorage(context.getSharedPreferences(Constants.PLAYLIST_MAKER_PREFS, Context.MODE_PRIVATE))
        )
    }

    fun provideThemeSwitchInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }
}
