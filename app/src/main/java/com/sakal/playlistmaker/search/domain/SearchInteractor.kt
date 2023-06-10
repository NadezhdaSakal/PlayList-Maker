package com.sakal.playlistmaker.search.domain

interface SearchInteractor{
    fun clearHistory()
    fun getHistory(): List<Track>
    fun loadTracks(query: String)
    fun writeHistory(historyTracks: List<Track>)
    fun unsubscribeFromTracksLoadResult()
    fun subscribeOnTracksLoadResult(listener: TracksLoadResultListener)
}