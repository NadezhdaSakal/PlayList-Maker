package com.sakal.playlistmaker.search.domain

import com.sakal.playlistmaker.utils.Resource

interface TracksRepo {
    fun searchTracks(query: String): Resource<ArrayList<Track>>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): ArrayList<Track>
}