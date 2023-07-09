package com.sakal.playlistmaker.di

import com.sakal.playlistmaker.player.data.impl.PlayerRepoImpl
import com.sakal.playlistmaker.player.domain.PlayerRepo
import com.sakal.playlistmaker.search.data.impl.TracksRepoImpl
import com.sakal.playlistmaker.search.domain.TracksRepo
import com.sakal.playlistmaker.settings.data.impl.SettingsRepoImpl
import com.sakal.playlistmaker.settings.domain.SettingsRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {

    singleOf(::TracksRepoImpl).bind<TracksRepo>()

    factoryOf(::PlayerRepoImpl).bind<PlayerRepo>()

    singleOf(::SettingsRepoImpl).bind<SettingsRepository>()
}