package com.sakal.playlistmaker.di

import com.sakal.playlistmaker.media_library.ui.viewmodels.BottomSheetViewModel
import com.sakal.playlistmaker.media_library.ui.viewmodels.PlaylistViewModel
import com.sakal.playlistmaker.media_library.ui.viewmodels.FavoritesFragmentViewModel
import com.sakal.playlistmaker.media_library.ui.viewmodels.PlaylistsViewModel
import com.sakal.playlistmaker.new_playlist.ui.viewmodels.NewPlaylistViewModel
import com.sakal.playlistmaker.player.ui.viewmodel.AudioPlayerViewModel
import com.sakal.playlistmaker.search.ui.viewmodel.SearchViewModel
import com.sakal.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val viewModelModule = module {

    viewModelOf(::SearchViewModel).bind()

    viewModelOf(::AudioPlayerViewModel).bind()

    viewModelOf(::SettingsViewModel).bind()

    viewModelOf(::FavoritesFragmentViewModel).bind()

    viewModelOf(::PlaylistsViewModel).bind()

    viewModelOf(::NewPlaylistViewModel).bind()

    viewModelOf(::BottomSheetViewModel).bind()

    viewModelOf(::PlaylistViewModel)

}