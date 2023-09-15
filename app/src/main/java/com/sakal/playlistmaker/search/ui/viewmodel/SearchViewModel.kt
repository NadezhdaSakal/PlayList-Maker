package com.sakal.playlistmaker.search.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.search.domain.TracksInteractor
import com.sakal.playlistmaker.search.ui.state.SearchScreenState
import com.sakal.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {

    private val _screenState = MutableLiveData<SearchScreenState>()
    fun observeState(): LiveData<SearchScreenState> = _screenState
    var isClickable = true

    init {
        val history = showHistory()
        if (history.isNotEmpty()) {
            renderState(SearchScreenState.ShowHistory(history))
        }
    }

    private val tracksSearchDebounce =
        debounce<String>(Constants.SEARCH_DEBOUNCE_DELAY_MILLIS, viewModelScope, true) { query ->
            getTracks(query)
        }

    private val trackClickDebounce =
        debounce<Boolean>(Constants.CLICK_DEBOUNCE_DELAY_MILLIS, viewModelScope, false) {
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

    private fun processResult(foundTracks: List<Track>?, error: Int?) {
        var tracks = listOf<Track>()
        if (foundTracks != null) {
            tracks = foundTracks
        }

        when {
            error != null -> {
                renderState(SearchScreenState.Error(error = error))
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
            renderState(SearchScreenState.Success(listOf()))
        }
    }

    fun clearHistory() {
        tracksInteractor.clearHistory()
        _screenState.postValue(SearchScreenState.Success(listOf()))
    }

    private fun showHistory(): List<Track> {
        return tracksInteractor.getHistory()
    }

    private fun renderState(state: SearchScreenState) {
        _screenState.postValue(state)
    }
}