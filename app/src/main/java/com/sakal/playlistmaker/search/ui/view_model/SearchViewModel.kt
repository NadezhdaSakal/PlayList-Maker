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
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.creator.Creator
import com.sakal.playlistmaker.search.domain.TracksInteractor

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val tracksInteractor = Creator.provideTracksInteractor(getApplication<Application>())
    private val _screenState = MutableLiveData<SearchScreenState>()

    fun observeState(): LiveData<SearchScreenState> = _screenState

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

    fun getTracks(query: String? = lastQuery) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        query?.let {
            _screenState.postValue(SearchScreenState.Loading)
            tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, errorMessage: String?, code: Int) {
                    when (code) {
                        ApiConstants.SUCCESS_CODE -> {
                            val tracks = arrayListOf<Track>()
                            if (foundTracks!!.isNotEmpty()) {
                                tracks.addAll(foundTracks)

                                _screenState.postValue(SearchScreenState.Success(tracks = tracks))
                            } else {
                                _screenState.postValue(SearchScreenState.NothingFound)
                            }
                        }
                        else -> {
                            _screenState.postValue(
                                SearchScreenState.Error(
                                    message = getApplication<Application>().getString(R.string.check_internet_connection),
                                )
                            )
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
        _screenState.postValue(SearchScreenState.Success(arrayListOf()))
    }

    fun showHistory() {
        if (tracksInteractor.getHistory().isNotEmpty()) {
            _screenState.value =
                SearchScreenState.ShowHistory(tracksInteractor.getHistory() as ArrayList<Track>)
        } else {
            _screenState.value = SearchScreenState.Success(arrayListOf())
        }
    }

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}
