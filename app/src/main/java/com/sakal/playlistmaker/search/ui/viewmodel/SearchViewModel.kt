package com.sakal.playlistmaker.search.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.search.domain.TracksInteractor
import com.sakal.playlistmaker.search.ui.SearchScreenState
import com.sakal.playlistmaker.utils.debounce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {

    private val _screenState = MutableLiveData<SearchScreenState>()
    private val history = ArrayList<Track>()
    fun observeState(): LiveData<SearchScreenState> = _screenState
    var isClickable = true

    init {
        history.addAll(tracksInteractor.getHistory())
        if (history.isNotEmpty()) {
            renderState(SearchScreenState.ShowHistory(history))
        }
    }

    private val tracksSearchDebounce =
        debounce<String>(Constants.SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { query ->
            getTracks(query)
        }

    private val trackClickDebounce =
        debounce<Boolean>(Constants.CLICK_DEBOUNCE_DELAY, viewModelScope, false) {
            isClickable = it
        }

    fun searchDebounce(query: String) {
        if (query.isNotEmpty()) {
            tracksSearchDebounce(query)
        }
    }

    fun onTrackClick() {
        isClickable = false
        trackClickDebounce(true)
    }

    fun getTracks(query: String) {
        if (query.isNotEmpty()) {

            renderState(SearchScreenState.Loading)

            viewModelScope.launch {
                tracksInteractor.searchTracks(query)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundTracks: ArrayList<Track>?, errorMessage: String?) {
        val tracks = arrayListOf<Track>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks)
        }

        when {
            errorMessage != null -> {
                renderState(SearchScreenState.Error(message = errorMessage))
            }

            tracks.isEmpty() -> {
                renderState(SearchScreenState.NothingFound)
            }

            else -> {
                renderState(SearchScreenState.Success(tracks = tracks))
            }
        }
    }


    fun addToHistory(track: Track) {
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
}