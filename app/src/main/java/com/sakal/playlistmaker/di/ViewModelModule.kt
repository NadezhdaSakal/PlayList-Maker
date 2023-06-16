package com.sakal.playlistmaker.di

import com.sakal.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import com.sakal.playlistmaker.search.ui.view_model.SearchViewModel
import com.sakal.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.sakal.playlistmaker.main.ui.view_model.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val viewModelModule = module {

    viewModelOf(::MainViewModel).bind()

    viewModelOf(::SearchViewModel).bind()

    viewModelOf(::AudioPlayerViewModel).bind()

    viewModelOf(::SettingsViewModel).bind()

}