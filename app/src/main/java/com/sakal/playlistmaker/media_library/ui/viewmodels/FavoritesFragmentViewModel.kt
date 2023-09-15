package com.sakal.playlistmaker.media_library.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.media_library.domain.FavoritesInteractor
import com.sakal.playlistmaker.media_library.ui.state.FavoritesState
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.utils.debounce
import kotlinx.coroutines.launch


class FavoritesFragmentViewModel(
    private val interactor: FavoritesInteractor
) : ViewModel() {

    private val contentStateLiveData = MutableLiveData<FavoritesState>()
    fun observeContentState(): LiveData<FavoritesState> = contentStateLiveData

    var isClickable = true

    private val trackClickDebounce =
        debounce<Boolean>(Constants.CLICK_DEBOUNCE_DELAY_MILLIS, viewModelScope, false) {
            isClickable = it
        }

    init {
        getFavoritesTracks()
    }

    fun getFavoritesTracks() {
        viewModelScope.launch {
            interactor
                .getFavoritesTracks()
                .collect { favoritesTracks ->
                    processResult(favoritesTracks)
                }
        }
    }

    private fun processResult(trackList: List<Track>) {
        when {
            trackList.isEmpty() -> {
                contentStateLiveData.value = FavoritesState.Empty
            }
            else -> {
                contentStateLiveData.value = FavoritesState.FavoritesTracks(trackList)
            }
        }
    }

    fun onTrackClick() {
        isClickable = false
        trackClickDebounce(true)
    }
}
