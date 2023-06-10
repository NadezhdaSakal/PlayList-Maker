package com.sakal.playlistmaker.search.domain

interface SearchRepo {
    var tracksLoadResultListener: TracksLoadResultListener?

    fun loadTracks(query: String)
}