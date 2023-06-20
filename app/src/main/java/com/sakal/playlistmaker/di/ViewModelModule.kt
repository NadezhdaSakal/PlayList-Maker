package com.sakal.playlistmaker.di

import com.sakal.playlistmaker.player.ui.viewmodel.AudioPlayerViewModel
import com.sakal.playlistmaker.search.ui.viewmodel.SearchViewModel
import com.sakal.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import com.sakal.playlistmaker.main.ui.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val viewModelModule = module {

    viewModelOf(::MainViewModel).bind()

    viewModelOf(::SearchViewModel).bind()

    viewModelOf(::AudioPlayerViewModel).bind()

    viewModelOf(::SettingsViewModel).bind()

}