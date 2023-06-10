package com.sakal.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.search.domain.SearchInteractor
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.search.domain.TracksLoadResultListener
import com.sakal.playlistmaker.search.ui.SearchState


class SearchViewModel(
    private val interactor: SearchInteractor,
) : ViewModel() {

    private val historyTracks = mutableListOf<Track>()
    private val stateSearch = MutableLiveData<SearchState>()
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private var lastQuery: String? = null

    val state: LiveData<SearchState> = stateSearch

    init {
        interactor.subscribeOnTracksLoadResult(object : TracksLoadResultListener {
            override fun onSuccess(tracks: List<Track>) {
                if (tracks.isEmpty()) {
                    stateSearch.postValue(SearchState.Empty)
                } else {
                    stateSearch.postValue(SearchState.Tracks(tracks))
                }
            }

            override fun onError() {
                stateSearch.postValue(SearchState.Error)
            }
        })

        historyTracks.addAll(interactor.getHistory())
    }

    fun searchFocusChanged(hasFocus: Boolean, text: String) {
        val historyTracks = interactor.getHistory()
        if (hasFocus && text.isEmpty() && historyTracks.isNotEmpty()) {
            stateSearch.postValue(SearchState.History(historyTracks))
        }
    }

    fun loadTracks(query: String) {
        if (query.isEmpty()) {
            return
        }
        stateSearch.postValue(SearchState.Loading)
        interactor.loadTracks(
            query = query
        )
    }

    fun showHistoryTracks() {
        if (interactor.getHistory().isNotEmpty()) {
            stateSearch.value = SearchState.History(interactor.getHistory())
        } else {
            stateSearch.value = SearchState.Empty
        }
    }

    fun onDestroyView() {
        interactor.unsubscribeFromTracksLoadResult()
    }

    fun clearHistory() {
        interactor.clearHistory()
        stateSearch.postValue(SearchState.History(interactor.getHistory()))
    }

    fun clearSearchText() {
        stateSearch.postValue(SearchState.History(interactor.getHistory()))
    }

    fun openTrack(track: Track) {
        if (historyTracks.contains(track)) {
            historyTracks.remove(track)
            historyTracks.add(Constants.INDEX_FIRST, track)
        } else {
            historyTracks.add(Constants.INDEX_FIRST, track)
        }
        if (historyTracks.size > Constants.HISTORY_LIST_SIZE) {
            historyTracks.removeLast()
        }
        interactor.writeHistory(historyTracks)
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, Constants.CLICK_DEBOUNCE_DELAY)
        }
        return current
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

    private fun makeDelaySearching(changedText: String) {
        val searchRunnable = Runnable { loadTracks(changedText) }
        val postTime = SystemClock.uptimeMillis() + Constants.SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    companion object {
        val SEARCH_REQUEST_TOKEN = Any()
    }

}