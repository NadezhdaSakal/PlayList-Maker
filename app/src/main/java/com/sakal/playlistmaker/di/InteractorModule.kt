package com.sakal.playlistmaker.di

import com.sakal.playlistmaker.media_library.domain.FavoritesInteractor
import com.sakal.playlistmaker.media_library.domain.PlaylistsInteractor
import com.sakal.playlistmaker.media_library.domain.impl.FavoritesInteractorImpl
import com.sakal.playlistmaker.media_library.domain.impl.PlaylistsInteractorImpl
import com.sakal.playlistmaker.player.domain.PlayerInteractor
import com.sakal.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.sakal.playlistmaker.search.domain.TracksInteractor
import com.sakal.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.sakal.playlistmaker.settings.domain.SettingsInteractor
import com.sakal.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val interactorModule = module {

    singleOf(::TracksInteractorImpl).bind<TracksInteractor>()

    singleOf(::PlayerInteractorImpl).bind<PlayerInteractor>()

    singleOf(::SettingsInteractorImpl).bind<SettingsInteractor>()

    singleOf(::FavoritesInteractorImpl).bind<FavoritesInteractor>()

    singleOf(::PlaylistsInteractorImpl).bind<PlaylistsInteractor>()

}