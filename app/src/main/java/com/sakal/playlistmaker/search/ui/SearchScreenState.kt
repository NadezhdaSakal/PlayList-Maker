package com.sakal.playlistmaker.search.ui

import com.sakal.playlistmaker.search.domain.Track

sealed interface SearchScreenState {

    object Loading : SearchScreenState

    object NothingFound : SearchScreenState

    data class Success(
        val tracks: ArrayList<Track>
    ) : SearchScreenState

    data class ShowHistory(
        val tracks: ArrayList<Track>
    ) : SearchScreenState

    data class Error(
        val message: String
    ) : SearchScreenState

}