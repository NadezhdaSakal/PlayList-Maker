package com.sakal.playlistmaker.di

import com.sakal.playlistmaker.media_library.ui.viewmodels.FavoritesFragmentViewModel
import com.sakal.playlistmaker.media_library.ui.viewmodels.PlaylistCreatorViewModel
import com.sakal.playlistmaker.media_library.ui.viewmodels.PlaylistMenuBottomSheetViewModel
import com.sakal.playlistmaker.media_library.ui.viewmodels.PlaylistViewModel
import com.sakal.playlistmaker.media_library.ui.viewmodels.PlaylistsBottomSheetViewModel
import com.sakal.playlistmaker.media_library.ui.viewmodels.PlaylistsViewModel
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

    viewModelOf(::PlaylistCreatorViewModel).bind()

    viewModelOf(::PlaylistsBottomSheetViewModel).bind()

    viewModelOf(::PlaylistViewModel)

    viewModelOf(::PlaylistMenuBottomSheetViewModel)

}