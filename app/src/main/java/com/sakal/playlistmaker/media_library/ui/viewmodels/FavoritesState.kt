package com.sakal.playlistmaker.media_library.ui.viewmodels

import com.sakal.playlistmaker.search.domain.Track

sealed interface FavoritesState {
    object Empty : FavoritesState

    data class FavoritesTracks(
        val tracks: List<Track>,
    ) : FavoritesState
}