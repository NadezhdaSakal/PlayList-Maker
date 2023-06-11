package com.sakal.playlistmaker.search.ui

import com.sakal.playlistmaker.search.domain.Track

sealed class SearchState {

    object Loading : SearchState()
    data class History(val tracks: List<Track>, val clearText:Boolean = false) : SearchState()
    data class Tracks(val tracks: List<Track>) : SearchState()
    object Error : SearchState()
    object Empty : SearchState()


}