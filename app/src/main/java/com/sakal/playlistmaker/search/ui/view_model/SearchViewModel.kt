package com.sakal.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.search.ui.SearchScreenState
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sakal.playlistmaker.ApiConstants
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.creator.Creator
import com.sakal.playlistmaker.search.domain.TracksInteractor

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val tracksInteractor = Creator.provideTracksInteractor(getApplication<Application>())
    private val _screenState = MutableLiveData<SearchScreenState>()
    val screenState: LiveData<SearchScreenState> = _screenState
    private val handler = Handler(Looper.getMainLooper())
    private var lastQuery: String? = null
    private var isClickAllowed = true

    private fun makeDelaySearching(changedText: String) {
        val searchRunnable = Runnable { getTracks(changedText) }
        val postTime = SystemClock.uptimeMillis() + Constants.SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun trackOnClickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, Constants.CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun onDestroy() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
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

    fun getTracks (query: String? = lastQuery) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        query?.let {
            _screenState.postValue(SearchScreenState.Loading())
            tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, errorMessage: String?, code: Int) {
                    when (code) {
                        ApiConstants.SUCCESS_CODE -> {
                            if (foundTracks!!.isNotEmpty()) {
                                _screenState.postValue(SearchScreenState.Success(foundTracks))
                            } else {
                                _screenState.postValue(SearchScreenState.NothingFound())
                            }
                        }
                        else -> {
                            _screenState.postValue(SearchScreenState.Error())
                        }
                    }
                }
            })
        }
    }

    fun addToHistory(track: Track) {
        tracksInteractor.addTrackToHistory(track)
    }

    fun clearHistory() {
        tracksInteractor.clearHistory()
        _screenState.postValue(SearchScreenState.ShowHistory(null))
    }

    fun showHistory() {
        if (tracksInteractor.getHistory().isNotEmpty()) {
            _screenState.value = SearchScreenState.ShowHistory(tracksInteractor.getHistory())
        } else {
            _screenState.value = SearchScreenState.Success(null)
        }
    }

    fun isReadyToRender(screenState: SearchScreenState, queryText: String): Boolean {
        if ((screenState is SearchScreenState.Success
                    && queryText.isNotEmpty())
            || (screenState !is SearchScreenState.Success)
        ) {
            return true
        }
        return false
    }

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}
