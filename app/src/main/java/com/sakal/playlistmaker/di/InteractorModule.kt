package com.sakal.playlistmaker.di

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

}