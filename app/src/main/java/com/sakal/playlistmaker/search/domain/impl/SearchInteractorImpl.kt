package com.sakal.playlistmaker.search.domain.impl

import com.sakal.playlistmaker.search.domain.HistoryDataStore
import com.sakal.playlistmaker.search.domain.SearchInteractor
import com.sakal.playlistmaker.search.domain.SearchRepo
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.search.domain.TracksLoadResultListener

class SearchInteractorImpl(
    private val historySearchDataStore: HistoryDataStore,
    private val repository: SearchRepo, ) : SearchInteractor {

    override fun clearHistory() {
        historySearchDataStore.clearHistory()
    }

    override fun getHistory(): List<Track> {
        return historySearchDataStore.getHistory()
    }

    override fun loadTracks(query: String) {
        repository.loadTracks(query)
    }

    override fun writeHistory(historyTracks: List<Track>) {
        historySearchDataStore.writeHistory(historyTracks)
    }

    override fun subscribeOnTracksLoadResult(listener: TracksLoadResultListener) {
        repository.tracksLoadResultListener = listener
    }

    override fun unsubscribeFromTracksLoadResult() {
        repository.tracksLoadResultListener = null
    }
}