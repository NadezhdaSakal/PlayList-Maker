package com.sakal.playlistmaker.search.domain

interface TracksInteractor {
    fun searchTracks(query: String, consumer: TracksConsumer)

    interface TracksConsumer{
        fun consume(foundTracks: ArrayList<Track>?, errorMessage: String?)
    }

    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): ArrayList<Track>

}