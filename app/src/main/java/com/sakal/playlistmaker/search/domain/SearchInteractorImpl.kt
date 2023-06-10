package com.sakal.playlistmaker.search.domain

class SearchInteractorImpl(
    private val historySearchDataStore: HistoryDataStore,
    private val repository: SearchRepo,
) : SearchInteractor {
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