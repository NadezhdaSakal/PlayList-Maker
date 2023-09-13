package com.sakal.playlistmaker.search.ui.state

import com.sakal.playlistmaker.search.domain.Track

sealed interface SearchScreenState {

    object Loading : SearchScreenState

    object NothingFound : SearchScreenState

    data class Success(
        val tracks: List<Track>
    ) : SearchScreenState

    data class ShowHistory(
        val tracks: List<Track>
    ) : SearchScreenState

    data class Error(
        val error: Int?
    ) : SearchScreenState

}