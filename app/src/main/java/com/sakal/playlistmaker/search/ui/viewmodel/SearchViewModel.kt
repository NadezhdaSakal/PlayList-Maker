package com.sakal.playlistmaker.search.ui.viewmodel

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.search.domain.TracksInteractor
import com.sakal.playlistmaker.search.ui.SearchScreenState

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {

    private val _screenState = MutableLiveData<SearchScreenState>()
    private val history = ArrayList<Track>()

    fun observeState(): LiveData<SearchScreenState> = _screenState

    private val handler = Handler(Looper.getMainLooper())
    private var lastQuery: String? = null
    private val _trackIsClickable = MutableLiveData(true)
    var trackIsClickable: LiveData<Boolean> = _trackIsClickable

    init {
        history.addAll(tracksInteractor.getHistory())
        if(history.isNotEmpty()) {
            renderState(SearchScreenState.ShowHistory(history))
        }
    }

    private fun makeDelaySearching(changedText: String) {
        val searchRunnable = Runnable { getTracks(changedText) }
        val postTime = SystemClock.uptimeMillis() + Constants.SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun searchDebounce(changedText: String? = lastQuery) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        if (!changedText.isNullOrEmpty()) {
            if ((lastQuery == changedText)) {
                return
            }
            this.lastQuery = changedText
            makeDelaySearching(changedText)
        }
    }

    fun onSearchClicked(track: Track) {
        trackOnClickDebounce()
        addToHistory(track)
    }

    private fun trackOnClickDebounce() {
        _trackIsClickable.value = false
        handler.postDelayed({ _trackIsClickable.value = true }, Constants.CLICK_DEBOUNCE_DELAY)
    }

    fun onDestroy() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun getTracks(query: String) {
        if (query.isNotEmpty()) {

            handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

            renderState(SearchScreenState.Loading)

            tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: ArrayList<Track>?, errorMessage: String?) {

                    val tracks = arrayListOf<Track>()
                    if (foundTracks != null) {
                        tracks.addAll(foundTracks)
                    }

                    when {
                        errorMessage != null -> {
                            renderState(
                                SearchScreenState.Error(
                                    message = errorMessage
                                )
                            )
                        }

                        tracks.isEmpty() -> {
                            renderState(
                                SearchScreenState.NothingFound
                            )
                        }

                        else -> {
                            renderState(
                                SearchScreenState.Success(
                                    tracks = tracks
                                )
                            )
                        }
                    }
                }
            })
        }
    }

    private fun addToHistory(track: Track) {
        tracksInteractor.addTrackToHistory(track)
    }

    fun clearSearch() {
        val historyTracks = showHistory()
        if (historyTracks.isNotEmpty()) {
            renderState(SearchScreenState.ShowHistory(historyTracks))
        } else {
            renderState(SearchScreenState.Success(arrayListOf()))
        }
    }

    fun clearHistory() {
        tracksInteractor.clearHistory()
        _screenState.postValue(SearchScreenState.Success(arrayListOf()))
    }

    private fun showHistory(): ArrayList<Track> {
        return tracksInteractor.getHistory()
    }

    private fun renderState(state: SearchScreenState) {
        _screenState.postValue(state)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}
